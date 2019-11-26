package com.yyw.cloud.study.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yyw
 * @date 2019/11/26
 */

@Configuration

public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule() {
        //负载均衡规则，改为随机
        return new RandomRule();
    }
}
