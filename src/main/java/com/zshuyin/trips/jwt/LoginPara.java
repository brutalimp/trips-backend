package com.zshuyin.trips.jwt;

public class LoginPara {

    private String name;
    private String password;


    public String getName() {
        return this.name;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}