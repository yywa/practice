package com.yyw.cloud.study.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author yyw
 * @date 2019/11/26
 */

@Configuration
@RibbonClient(name = "microservice-provider-server", configuration = RibbonConfiguration.class)
public class TestConfiguration {
}
