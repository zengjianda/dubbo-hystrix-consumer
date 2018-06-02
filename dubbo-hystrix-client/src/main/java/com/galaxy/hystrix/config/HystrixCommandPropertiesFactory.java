package com.galaxy.hystrix.config;

import com.alibaba.dubbo.common.URL;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 命令相关配置
 */
public class HystrixCommandPropertiesFactory {
    //设置熔断条件
    public static HystrixCommandProperties.Setter create(URL url, String method) {
        return HystrixCommandProperties.Setter().withCircuitBreakerSleepWindowInMilliseconds(url.getMethodParameter(method, "sleepWindowInMilliseconds", 5000))//	熔断后sleepWindowInMilliseconds毫秒会放入一个请求，如果请求处理成功，熔断器关闭，否则熔断器打开，继续等待sleepWindowInMilliseconds
                .withCircuitBreakerErrorThresholdPercentage(url.getMethodParameter(method, "errorThresholdPercentage", 50))//超过50%错误触发熔断
                .withCircuitBreakerRequestVolumeThreshold(url.getMethodParameter(method, "requestVolumeThreshold", 20))//一个统计周期内（默认10秒）请求不少于requestVolumeThreshold才会进行熔断判断
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutInMilliseconds(url.getMethodParameter(method, "timeoutInMilliseconds", 1000))//注意该时间和dubbo自己的超时时间不要冲突，以这个时间优先，比如consumer设置3秒，那么当执行时hystrix会提前超时
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(url.getMethodParameter(method, "fallbackMaxConcurrentRequests", 50))//并发调用fallback最大值
                .withExecutionIsolationStrategy(IsolationStrategy.getIsolationStrategy(url))
                .withExecutionIsolationSemaphoreMaxConcurrentRequests(url.getMethodParameter(method, "maxConcurrentRequests", 20));//最大并发数

    }
}
