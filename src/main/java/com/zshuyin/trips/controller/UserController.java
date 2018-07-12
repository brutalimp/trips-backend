package com.zshuyin.trips.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping()
    public String greeting() {
        return "test";
    }
}