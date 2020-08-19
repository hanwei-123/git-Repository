package com.jk.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Value("${my.name}")
    String name;

    //@Value("${my.gender}")
    String gender;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return  name + "------" + gender;
    }
}
