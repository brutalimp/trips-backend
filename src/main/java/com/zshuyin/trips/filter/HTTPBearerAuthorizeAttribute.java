package com.zshuyin.trips.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import io.jsonwebtoken.Claims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zshuyin.trips.bean.User;
import com.zshuyin.trips.jwt.Audience;
import com.zshuyin.trips.jwt.JwtHelper;
import com.zshuyin.trips.repository.UserRepository;
import com.zshuyin.trips.utils.ResultMsg;
import com.zshuyin.trips.utils.ResultStatusCode;

public class HTTPBearerAuthorizeAttribute implements Filter {

	@Autowired
	private Audience audienceEntity;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		ResultMsg resultMsg;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String method = httpRequest.getMethod();
		String auth = httpRequest.getHeader("Authorization");
		if (method.equals("OPTIONS")) {
			chain.doFilter(request, response);
			return;
		}
		if ((auth != null) && (auth.length() > 7)) {
			String HeadStr = auth.substring(0, 6).toLowerCase();
			if (HeadStr.compareTo("bearer") == 0) {

				auth = auth.substring(7, auth.length());
				Claims claims = JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret());
				if (claims != null) {
					String userid = claims.get("userid", String.class);
					Optional<User> result = userRepository.findById(userid);
					if (result.isPresent()) {
						request.setAttribute("user", result.get());
					}
					chain.doFilter(request, response);
					return;
				}
			}
		}

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json; charset=utf-8");
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ObjectMapper mapper = new ObjectMapper();

		resultMsg = new ResultMsg(ResultStatusCode.INVALID_TOKEN.getErrcode(),
				ResultStatusCode.INVALID_TOKEN.getErrmsg(), null);
		httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));
		return;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
