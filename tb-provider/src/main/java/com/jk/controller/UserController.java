package com.jk.controller;

import com.jk.entity.UserEntity;
import com.jk.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController implements UserService{

    @Resource
    private UserService userService;



    @Override
    @RequestMapping("/selectUserList")
    public List<UserEntity> selectUserList() {
        return userService.selectUserList();
    }
}
