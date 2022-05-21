package com.example.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.Appointment;
import com.example.springboot.entity.Message;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    MessageService messageService;
    @Autowired
    AppointmentService appointmentService;

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
                Message message = new Message();
                message.setMTitle("欢迎来到蓝星智能教室！");
                message.setMContent("欢迎来到蓝星智能教室！首次登录后请尽快完善您的个人信息，绑定邮箱，以防影响您的正常使用。祝您学习生活愉快！");
                message.setMReceiverId(user.getUId());
                messageService.save(message);
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

    /**
     * 获取活跃用户占比
     * @return
     */
    public int getActiveRatio() {
        QueryWrapper<Appointment> appointmentQueryWrapper = new QueryWrapper<>();
        appointmentQueryWrapper.between("a_status", 1, 2);
        List<Appointment> appointmentList = appointmentService.list(appointmentQueryWrapper);
        List<Integer> userIds = new ArrayList<>();
        for (Appointment appointment: appointmentList) {
            if (userIds.size() == 0) {
                userIds.add(appointment.getUId());
            }
            for (Integer i: userIds) {
                if (!appointment.getUId().equals(i)) {
                    userIds.add(appointment.getUId());
                    break;
                }
            }
        }
        float total = list().size();
        int ratio = (int) (userIds.size() / total * 100);
        return ratio;
    }
}
