package com.example.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Constants;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_login_name", userDTO.getULoginName());
        queryWrapper.eq("u_password", userDTO.getUPassword());
        try {
            User one = getOne(queryWrapper);
            if (one != null) {
                BeanUtil.copyProperties(one, userDTO, true);
                return userDTO;
            } else {
                throw new ServiceException(Constants.CODE_402, "用户名或密码错误");
            }
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
    }
}
