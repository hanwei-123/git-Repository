package com.jk.service;

import com.jk.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> selectUserList();
}
