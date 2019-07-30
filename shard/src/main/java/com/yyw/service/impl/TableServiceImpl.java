package com.yyw.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.yyw.entity.Table;
import com.yyw.mapper.TableMapper;
import com.yyw.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yyw
 * @date 2019/7/17
 **/
@Service("tableService")
public class TableServiceImpl implements TableService {
    @Autowired
    private TableMapper tableMapper;

    @Override
    public String getTableName(String id) {
        Table tables = tableMapper.getTableName(id);
        return StringUtils.isEmpty(tables.getTableName()) ? null : tables.getTableName();
    }
}
