package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }
}
