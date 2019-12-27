package com.yyw.plugin;

import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

/**
 * @author yyw
 * @date 2019/12/27
 */
public class LombakPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return false;
    }
}
