package com.yyw.strategy;

import com.google.code.shardbatis.strategy.ShardStrategy;
import com.yyw.SpringContextAware;
import com.yyw.service.TableService;

import java.util.Map;

/**
 * @author yyw
 * @date 2019/7/17
 **/
public class MyShardStrategy implements ShardStrategy {
    public String getTargetTableName(String s, Object o, String s1) {
        //解析参数，获取需要的参数
        String id = ((Map<String, String>) o).get("id");
        //获取对应的Bean
        TableService tablesService = SpringContextAware.getBean("tablesService");
        return tablesService.getTableName(id);
    }
}
