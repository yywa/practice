package com.yyw.service.impl;

import com.yyw.entity.User;
import com.yyw.mapper.UserMapper;
import com.yyw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yyw
 * @date 2019/7/31
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<User> queryAll() {
        return mapper.queryAll();
    }

    /**
     * 获取用户策略：先从缓存中获取用户，没有则取数据表中的数据，再将数据写入缓存中
     *
     * @param id id
     * @return User
     */
    @Override
    public User findUserById(int id) {
        String key = "user_" + id;
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            User user = operations.get(key);
            System.out.println("========从缓存中获取数据==========");
            System.out.println(user.getUserName());
            System.out.println("==================================");
            return user;
        } else {
            User user = mapper.findUserById(id);
            System.out.println("==========从数据表中获取数据=======");
            System.out.println(user.getUserName());

            //写入缓存
            operations.set(key, user, 5, TimeUnit.HOURS);
            return user;
        }
    }

    /**
     * 更新用户策略：先更新数据表，成功之后，删除原来的缓存，再更新缓存。
     *
     * @param user user
     * @return int
     */
    @Override
    public int updateUser(User user) {
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        int result = mapper.updateUser(user);
        if (result != 0) {
            String key = "user_" + user.getId();
            Boolean hasKey = redisTemplate.hasKey(key);
            if (hasKey) {
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key===========>" + key);
            } else {
                User userNew = mapper.findUserById(user.getId());
                if (userNew != null) {
                    operations.set(key, userNew, 3, TimeUnit.HOURS);
                }
            }
        }
        return result;
    }

    /**
     * 删除用户策略：删除表中数据，然后删除缓存
     *
     * @param id id
     * @return int
     */
    @Override
    public int deleteUserById(int id) {
        int result = mapper.deleteUserById(id);
        if (result != 0) {
            String key = "user_" + id;
            Boolean hasKey = redisTemplate.hasKey(key);
            if (hasKey) {
                Boolean delete = redisTemplate.delete(key);
                if (delete) {
                    System.out.println("删除了缓存中的key" + key);
                }
            }
        }
        return result;
    }
}
