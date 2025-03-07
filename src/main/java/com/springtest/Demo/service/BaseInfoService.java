package com.springtest.Demo.service;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseInfoService {

    private final JwtService jwtService;

    @Autowired
    public BaseInfoService(JwtService jwtService) {
        this.jwtService = jwtService;
    }


}
