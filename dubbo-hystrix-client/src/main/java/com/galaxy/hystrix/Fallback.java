package com.galaxy.hystrix;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * 业务失败返回处理函数
 */
@SPI
public interface Fallback {
    Object invoke();
}
