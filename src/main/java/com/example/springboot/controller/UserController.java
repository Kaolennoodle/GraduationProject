package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")


public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getULoginName();
        String password = userDTO.getUPassword();
        System.out.println("Output from LoginController: " + userDTO);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password))
            return Result.error(Constants.CODE_400, "用户名或密码为空！");
        else
            return userService.register(userDTO);
    }

    //新增或编辑
    @PostMapping
    public boolean save(@RequestBody User user) { //新增或更新
        System.out.println("Output from UserController: " + user);
        return userService.saveUser(user);
    }

    //查询所有数据
    @GetMapping
    public List<User> findAll() {
        List<User> list = userService.list();
        System.out.println(list);
        return list;
    }

    //按u_id删除
    @DeleteMapping("/{c_id}")
    public boolean delete(@PathVariable Integer c_id) {
        return userService.removeById(c_id);
    }

    //批量删除
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> c_ids) {
        return userService.removeBatchByIds(c_ids);
    }

    //    分页查询-MyBatis-Plus
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String u_name, @RequestParam(defaultValue = "") String u_stu_num, @RequestParam(defaultValue = "") String u_nickname, @RequestParam(defaultValue = "") String u_phone, @RequestParam(defaultValue = "") String u_email, @RequestParam(defaultValue = "") String u_type, @RequestParam(defaultValue = "") String u_login_name) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        //模糊查询
        if (!u_name.equals("")) queryWrapper.like("u_name", u_name);
        if (!u_stu_num.equals("")) queryWrapper.eq("u_stu_num", u_stu_num);
        if (!u_nickname.equals("")) queryWrapper.like("u_nickname", u_nickname);
        if (!u_phone.equals("")) queryWrapper.eq("u_phone", u_phone);
        if (!u_email.equals("")) queryWrapper.eq("u_email", u_email);
        if (!u_type.equals("")) queryWrapper.eq("u_type", u_type);
        if (!u_login_name.equals("")) queryWrapper.eq("u_login_name", u_login_name);
        queryWrapper.orderByDesc("u_create_time");
        return userService.page(page, queryWrapper);
    }

    //重置用户密码
    @PostMapping("/reset/pwd/{u_id}")
    public boolean resetPwd(@PathVariable Integer u_id) {
        User user = new User();
        user.setUId(u_id);
        user.setUPassword("12345678");
        return userService.saveUser(user);
    }
}
