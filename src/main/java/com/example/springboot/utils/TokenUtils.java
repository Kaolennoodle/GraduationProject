package com.example.springboot.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class TokenUtils {

    private static UserService staticUserService;

    @Resource
    private UserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    //生成一个token
    public static String generateToken(String userId, String password) {
        return JWT.create().withAudience(userId) //将userid保存到token里面
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) //2小时后过期
                .sign(Algorithm.HMAC256(password)); //以password作为token的密钥
    }

    //从token获取当前用户
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            System.out.println("token: " + token);
            if(StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                System.out.println("userId: " + userId);
                User user = staticUserService.getById(Integer.valueOf(userId));
                System.out.println("user: " + user);
                return user;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
