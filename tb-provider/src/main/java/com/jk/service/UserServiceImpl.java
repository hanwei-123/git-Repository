package com.jk.service;

import com.jk.entity.UserEntity;
import com.jk.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserEntity> selectUserList() {
        return userMapper.selectUserList();
    }
}
