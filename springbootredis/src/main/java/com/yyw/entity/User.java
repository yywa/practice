package com.yyw.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yyw
 * @date 2019/7/31
 **/
@Data
public class User implements Serializable {
    private int id;
    private String userName;
    private String passWord;
    private int salary;
}
