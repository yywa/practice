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

    @Override
    public String getTargetTableName(String s, Object o, String s1) {
        //解析参数，获取需要的参数
        //post请求是这样取
//        String id = ((Map<String, String>) o).get("id");

        //get请求这样处理
        String id = o.toString();
        //获取对应的Bean
        TableService tablesService = SpringContextAware.getBean("tableService");
        return tablesService.getTableName(id);
    }
}
