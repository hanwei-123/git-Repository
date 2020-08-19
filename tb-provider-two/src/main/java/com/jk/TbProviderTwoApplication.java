package com.jk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TbProviderTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TbProviderTwoApplication.class, args);
    }

}
