package com.yyw.service;

/**
 * @author yyw
 * @date 2019/7/17
 **/
public interface TableService {
    /**
     * 因为分表的表名对应的信息存放在tables表中，所以通过这个方法访问，通过对应的id获取对应的表名
     * @param id
     * @return
     */
    String getTableName(String id);
}
