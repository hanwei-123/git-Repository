package com.jk.service;

import com.jk.entity.UserEntity;
import com.jk.mapper.UserMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    @RequestMapping("/findUserList")
    public List<UserEntity> findUserList() {
        return userMapper.selectUserList();
    }

    @Override
    @RequestMapping("/hello")
    public String hello(@RequestParam String name) {
        return "hi, what is your name?  my name is " + name;
    }

    @Override
    @RequestMapping("/saveOrder")
    public Object saveOrder(@RequestParam Integer userId, @RequestParam Integer productId) {
        Map<String, Object> orderMap = new HashMap<String, Object>();
        orderMap.put("orderId", 11111);
        orderMap.put("userId", userId);
        orderMap.put("productId", productId);
        orderMap.put("orderNo", "20200815103622123");
        orderMap.put("orderName", "一架辽宁舰");
        orderMap.put("orderPrice", 100000);
        orderMap.put("createTime", "2020-08-15 10:37");
        return orderMap;
    }

}
