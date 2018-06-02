package com.galaxy.hystrix.controller;

import com.galaxy.hystrix.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiAction {
    @Autowired
    private HelloService helloService;

    @RequestMapping("sayHello")
    public String sayHello() {
        String value = helloService.sayHello();
        System.out.println(value);
        return value;
    }
}
