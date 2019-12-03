package com.yyw.study.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yyw.study.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

/**
 * @author yyw
 * @date 2019/12/3
 */
@Service
public class AggregationService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "fallback")
    public Observable<User> getUserById(Long id) {
        return Observable.create(observer -> {
            User user = restTemplate.getForObject("http://microservice-provider-user/{id}", User.class, id);
            observer.onNext(user);
            observer.onCompleted();
        });

    }

    @HystrixCommand(fallbackMethod = "fallback")
    public Observable<User> getMovieUserByUserId(Long id) {
        return Observable.create(observer -> {
            User user = restTemplate.getForObject("http://microservice-consumer-user/{id}", User.class, id);
            observer.onNext(user);
            observer.onCompleted();
        });
    }

    public User fallback(Long id) {
        User user = new User();
        user.setId(-1L);
        return user;
    }
}
