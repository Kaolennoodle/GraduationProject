package com.example.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.utils.TokenUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_login_name", userDTO.getULoginName());
        queryWrapper.eq("u_password", userDTO.getUPassword());
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_501, "系统错误（存在多个同名用户）");
        }
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);
            //设置token
            userDTO.setToken(TokenUtils.generateToken(one.getUId().toString(), one.getUPassword()));
            userDTO.setUPassword(null);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_402, "用户名或密码错误");
        }
    }

    public Result register(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_login_name", userDTO.getULoginName());
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_501, "系统错误（存在多个同名用户）");
        }
        if (one == null) {
            User user = new User();
            BeanUtil.copyProperties(userDTO, user, true);
            if (save(user)) {
                return new Result(Constants.CODE_200, "", null);
            }
        }
        return new Result(Constants.CODE_502, "该邮箱已被注册", null);
    }

    /**
     * 通过id获取用户姓名
     * @param u_id
     * @return
     */
    public User getNameById(Integer u_id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("u_name").eq("u_id", u_id);
        return getOne(queryWrapper);
    }
}
