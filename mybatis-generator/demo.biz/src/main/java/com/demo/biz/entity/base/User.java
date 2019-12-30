package com.demo.biz.entity.base;

import java.io.Serializable;
import lombok.Data;

@Data
public class User implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String usename;

    /**
     * 
     */
    private String password;

    private static final long serialVersionUID = 1L;
}