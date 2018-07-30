package com.zshuyin.trips.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.zshuyin.trips.bean.User;
import com.zshuyin.trips.jwt.AccessToken;
import com.zshuyin.trips.jwt.Audience;
import com.zshuyin.trips.jwt.JwtHelper;
import com.zshuyin.trips.jwt.LoginPara;
import com.zshuyin.trips.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.zshuyin.trips.utils.MyUtils;
import com.zshuyin.trips.utils.ResultMsg;
import com.zshuyin.trips.utils.ResultStatusCode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

@RestController
public class RegisterController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Audience audienceEntity;

    @PostMapping("/Register")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        Optional<User> result = userRepository.findByName(user.getName());
        if (result.isPresent()) {
            return ResponseEntity.status(403).build();
        }
        user.setPassword(MyUtils.getMD5(user.getPassword()));
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/Login")
    public ResponseEntity<ResultMsg> getUser(@RequestBody LoginPara loginPara) {
        ResultMsg resultMsg;
        try {
            Optional<User> user = userRepository.findByName(loginPara.getName());
            if (!user.isPresent()) {
                resultMsg = new ResultMsg(ResultStatusCode.INVALID_USERNAME.getErrcode(),
                        ResultStatusCode.INVALID_USERNAME.getErrmsg(), null);
                return ResponseEntity.status(404).body(resultMsg);
            } else {
                String md5Password = MyUtils.getMD5(loginPara.getPassword());

                if (md5Password.compareTo(user.get().getPassword()) != 0) {
                    resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getErrcode(),
                            ResultStatusCode.INVALID_PASSWORD.getErrmsg(), null);
                    return ResponseEntity.status(422).body(resultMsg);
                }
            }

            // 拼装accessToken
            String accessToken = JwtHelper.createJWT(loginPara.getName(), user.get().getId(), user.get().getRole(),
                    audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());

            // 返回accessToken
            AccessToken accessTokenEntity = new AccessToken();
            accessTokenEntity.setToken(accessToken);
            accessTokenEntity.setUser(user.get());
            resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(),
                    accessTokenEntity);
            return ResponseEntity.status(200).body(resultMsg);

        } catch (Exception ex) {
            resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg(),
                    null);
            return ResponseEntity.status(501).body(resultMsg);
        }
    }

    @GetMapping("/LoginByAuth")
    public ResponseEntity<ResultMsg> getUser(HttpServletRequest httpRequest) {
        String auth = httpRequest.getHeader("Authorization");
        if ((auth != null) && (auth.length() > 7)) {
            String HeadStr = auth.substring(0, 6).toLowerCase();
            if (HeadStr.compareTo("bearer") == 0) {
                auth = auth.substring(7, auth.length());
                Claims claims = JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret());
                if (claims != null) {
                    String userid = claims.get("userid", String.class);
                    Optional<User> result = userRepository.findById(userid);
                    if (result.isPresent()) {
                        return ResponseEntity.status(200).body(new ResultMsg(ResultStatusCode.OK.getErrcode(),
                                ResultStatusCode.OK.getErrmsg(), result.get()));
                    }
                }
            }
        }
        return ResponseEntity.status(401).body(new ResultMsg(ResultStatusCode.INVALID_TOKEN.getErrcode(),
                ResultStatusCode.INVALID_TOKEN.getErrmsg(), null));
    }
}