package com.jk.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "tb-provider-two", fallback = UserServiceFallback.class)
public interface UserServiceFeign extends UserService {


}
