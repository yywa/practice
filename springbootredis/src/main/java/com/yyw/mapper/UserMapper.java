package com.yyw.mapper;

import com.yyw.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yyw
 * @date 2019/7/31
 **/
@Mapper
public interface UserMapper {
    List<User> queryAll();

    User findUserById(int id);

    int updateUser(@Param("user") User user);

    int deleteUserById(int id);
}
