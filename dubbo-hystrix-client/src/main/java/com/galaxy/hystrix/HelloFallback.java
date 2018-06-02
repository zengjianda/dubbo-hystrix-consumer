package com.galaxy.hystrix;

import org.springframework.stereotype.Service;

@Service("helloFallback")
public class HelloFallback implements Fallback{
    @Override
    public Object invoke() {
        return "hystrix back";
    }
}
