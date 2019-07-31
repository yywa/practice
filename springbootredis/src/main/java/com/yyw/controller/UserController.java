package com.yyw.controller;

import com.yyw.entity.User;
import com.yyw.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author yyw
 * @date 2019/7/31
 **/
@RestController
@RequestMapping("/user")
@Api(value = "user查询相关接口")
public class UserController {
    @Autowired
    private UserService service;

    @RequestMapping("/findUserById")
    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int")
    public User findUserById(@RequestParam("id") int id) {
        return service.findUserById(id);
    }
}
