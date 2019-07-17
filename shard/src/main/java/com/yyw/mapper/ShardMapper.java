package com.yyw.mapper;

import com.yyw.entity.Shard;

/**
 * @author yyw
 * @date 2019/7/17
 **/
public interface ShardMapper {
    /**
     * 用来查询
     *
     * @param id
     * @return
     */
    Shard getShardResult(String id);
}
