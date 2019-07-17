package com.yyw.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.yyw.entity.Shard;
import com.yyw.mapper.ShardMapper;
import com.yyw.service.ShardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yyw
 * @date 2019/7/17
 **/
@Service("shardService")
public class ShardServiceImpl implements ShardService {
    @Autowired
    ShardMapper shardMapper;

    public String getName(String id) {
        //执行到这条语句时，会被拦截进入到分表策略MyShardStrategy.java中，获取对应的表名称，并且更改sql中的表名
        Shard shard = shardMapper.getShardResult(id);
        return StringUtils.isEmpty(shard.getName()) ? null : shard.getName();
    }
}
