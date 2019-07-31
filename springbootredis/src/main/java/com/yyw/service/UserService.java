package com.yyw.service;

import com.yyw.entity.User;

import java.util.List;

/**
 * @author yyw
 * @date 2019/7/31
 **/
public interface UserService {
    List<User> queryAll();

    User findUserById(int id);

    int updateUser(User user);

    int deleteUserById(int id);
}
