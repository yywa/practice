package com.yyw.config;

import com.google.code.shardbatis.plugin.ShardPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author yyw
 * @date 2019/7/17
 **/
@Configuration
public class ShardConfiguration {
    @Bean(name = "shardPlugin")
    public ShardPlugin shardPlugin() {
        ShardPlugin shardPlugin = new ShardPlugin();
        Properties properties = new Properties();
        //文件加载--键值必须为shardingConfig，这是类的内部要求，否则加载失败
        properties.put("shardingConfig", "shard/shard_config.xml");
        shardPlugin.setProperties(properties);
        return shardPlugin;
    }
}
