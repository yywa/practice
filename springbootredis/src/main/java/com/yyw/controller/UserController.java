package com.yyw.controller;

import com.yyw.entity.User;
import com.yyw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yyw
 * @date 2019/7/31
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

    @RequestMapping("/findUserById")
    public User findUserById(@RequestParam int id) {
        return service.findUserById(id);
    }
}
