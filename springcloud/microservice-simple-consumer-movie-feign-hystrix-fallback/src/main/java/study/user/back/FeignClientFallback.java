package study.user.back;

import org.springframework.stereotype.Component;
import study.user.entity.User;
import study.user.service.UserFeignClient;

/**
 * @author yyw
 * @date 2019/11/28
 */

@Component
public class FeignClientFallback implements UserFeignClient {
    @Override
    public User findById(Long id) {
        User user = new User();
        user.setUsername("默认用户");
        user.setId(-1L);
        return null;
    }
}
