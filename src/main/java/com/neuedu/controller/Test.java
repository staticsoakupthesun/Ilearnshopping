package com.neuedu.controller;

import com.neuedu.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class Test {

    @RequestMapping("/login.do")
    public UserInfo login(){
        UserInfo userInfo=new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("lisi");
        return userInfo;
    }
}

