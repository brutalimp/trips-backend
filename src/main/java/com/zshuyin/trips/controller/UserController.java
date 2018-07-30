package com.zshuyin.trips.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.zshuyin.trips.bean.User;
import com.zshuyin.trips.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/Users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/Users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/Users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/Users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") String userId, @Valid @RequestBody User user) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User updatedUser = userRepository.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") String userId) {

        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            userRepository.delete(existingUser.get());
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}