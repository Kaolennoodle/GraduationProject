package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

    public boolean login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_login_name", userDTO.getUsername());
        queryWrapper.eq("u_password", userDTO.getPassword());
        User one = getOne(queryWrapper);
        return one != null;
    }
}
