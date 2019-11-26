package study.user.service;

import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import study.user.config.FeignConfiguration;
import study.user.entity.User;

/**
 * @author yyw
 * @date 2019/11/26
 */
@FeignClient(name = "microservice-provider-user", configuration = FeignConfiguration.class)
public interface UserFeignClient {

    @RequestLine("GET /{id}")
    User findById(@Param("id") Long id);
}
