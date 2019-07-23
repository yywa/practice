package com.yyw;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author yyw
 * @date 2019/7/17
 **/
@Component
public class SpringContextAware implements ApplicationContextAware {
    //使用静态将其存储下来
    private static ApplicationContext applicationContexts;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContexts = applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContexts.getBean(name);
    }
}
