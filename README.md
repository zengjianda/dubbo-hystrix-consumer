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



