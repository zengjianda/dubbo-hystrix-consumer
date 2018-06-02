# dubbo-hystrix-consumer
dubbo服务集成Hystrix，客户端


maven.galaxy.com maven私服地址
zookeeper.galaxy.com zookeeper地址

集成Hystrix需要按照以下步骤：
1.写一个Filter实现dubbo的Filter
在invoke中加上Hystrix的熔断逻辑：如HystrixFilter

2.在resources目录下增加META-INF.dubbo增加文件com.alibaba.dubbo.rpc.Filter
内容：hystrix=com.galaxy.hystrix.HystrixFilter
配置好了Filter

3.写一个Command继承HystrixCommand，如DubboCommand
实现run方法和getFallback方法

4.利用SPI增加FallBack的实现
如HelloFallback
在resources目录下增加META-INF.dubbo增加文件*.Fallback
内容：helloFallback=com.galaxy.hystrix.HelloFallback

5. 调用dubbo服务的时候增加熔断
<dubbo:reference timeout="3000" interface="com.galaxy.hystrix.HelloService"
                     id="helloService" check="false">
        <dubbo:method name="sayHello">
            <dubbo:parameter key="fallback" value="helloFallback"/>
        </dubbo:method>
    </dubbo:reference>
根据方法名去增加配置，熔断条件有默认值，也可以通过配置文件设置，如
<dubbo:parameter key="errorThresholdPercentage" value="30"/>


HystrixCommandPropertiesFactory 设置熔断条件参数：
        return HystrixCommandProperties.Setter().withCircuitBreakerSleepWindowInMilliseconds(url.getMethodParameter(method, "sleepWindowInMilliseconds", 5000))//	熔断后sleepWindowInMilliseconds毫秒会放入一个请求，如果请求处理成功，熔断器关闭，否则熔断器打开，继续等待sleepWindowInMilliseconds
                .withCircuitBreakerErrorThresholdPercentage(url.getMethodParameter(method, "errorThresholdPercentage", 50))//超过50%错误触发熔断
                .withCircuitBreakerRequestVolumeThreshold(url.getMethodParameter(method, "requestVolumeThreshold", 20))//一个统计周期内（默认10秒）请求不少于requestVolumeThreshold才会进行熔断判断
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutInMilliseconds(url.getMethodParameter(method, "timeoutInMilliseconds", 1000))//注意该时间和dubbo自己的超时时间不要冲突，以这个时间优先，比如consumer设置3秒，那么当执行时hystrix会提前超时
                .withFallbackIsolationSemaphoreMaxConcurrentRequests(url.getMethodParameter(method, "fallbackMaxConcurrentRequests", 50))//并发调用fallback最大值
                .withExecutionIsolationStrategy(IsolationStrategy.getIsolationStrategy(url))
                .withExecutionIsolationSemaphoreMaxConcurrentRequests(url.getMethodParameter(method, "maxConcurrentRequests", 20));//最大并发数


