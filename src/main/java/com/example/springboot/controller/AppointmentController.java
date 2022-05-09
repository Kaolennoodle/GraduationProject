package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Appointment;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.User;
import com.example.springboot.service.AppointmentService;
import com.example.springboot.service.ClassroomService;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassroomService classroomService;

    /**
     * 新建预约
     *
     * @param uid
     * @param cid
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/new")
    public Result makeANew(@RequestParam Integer uid,
                           @RequestParam Integer cid,
                           @RequestParam String date,
                           @RequestParam String startTime,
                           @RequestParam String endTime) throws ParseException {
        return appointmentService.makeNew(uid, cid, date, startTime, endTime);
    }

    /**
     * 按a_id删除
     *
     * @param a_id
     * @return
     */
    @DeleteMapping("/{a_id}")
    public boolean delete(@PathVariable Integer a_id) {
        return appointmentService.removeById(a_id);
    }

    /**
     * 批量删除
     *
     * @param a_ids
     * @return
     */
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> a_ids) {
        return appointmentService.removeBatchByIds(a_ids);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param u_id
     * @param u_name
     * @param c_name
     * @param a_date
     * @param a_time
     * @param a_start_time
     * @param a_end_time
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") Integer u_id,
                           @RequestParam(defaultValue = "") String u_name,
                           @RequestParam(defaultValue = "") String c_name,
                           @RequestParam(defaultValue = "") String a_date,
                           @RequestParam(defaultValue = "") String a_time,
                           @RequestParam(defaultValue = "") String a_start_time,
                           @RequestParam(defaultValue = "") String a_end_time) {

        IPage<Appointment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Appointment> appointmentQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Classroom> classroomQueryWrapper = new QueryWrapper<>();

        List<User> userList;
        List<Classroom> classroomList;

        if (u_id != null)
            appointmentQueryWrapper.eq("u_id", u_id);

        if (u_name.length() > 0) {

            //仅在用户表中读出相关用户的u_id
            userQueryWrapper.select("u_id");
            userQueryWrapper.like("u_name", u_name);
            userList = userService.list(userQueryWrapper);

            //如果查询不到则返回错误
            if (userList.size() == 0)
                return Result.error(Constants.CODE_500, "系统中不存在姓名类似“" + u_name + "”的用户");

            //查询到u_id后将查询到的u_id放入u_ids中
            List<String> u_ids = new ArrayList<>();
            for (User user : userList) {
                u_ids.add(user.getUId().toString());
            }

            //使用QueryWapper.in查询所有传入用户相关的预约信息
            appointmentQueryWrapper.in("u_id", u_ids);
        }

        //教室查询逻辑跟用户查询类似
        if (c_name.length() > 0) {
            classroomQueryWrapper.select("c_id");
            classroomQueryWrapper.like("c_name", c_name);
            classroomList = classroomService.list(classroomQueryWrapper);
            if (classroomList.size() == 0)
                return Result.error(Constants.CODE_500, "系统中不存在名称类似“" + c_name + "”的教室");
            List<String> c_ids = new ArrayList<>();
            for (Classroom classroom : classroomList) {
                c_ids.add(classroom.getCId().toString());
            }
            appointmentQueryWrapper.in("c_id", c_ids);
        }

        //全部查询结束后，结果按照预约开始时间排序
        appointmentQueryWrapper.orderByAsc("a_start_time");
        return Result.success(appointmentService.page(page, appointmentQueryWrapper));
    }

    @GetMapping("/edit")
    public Result editAppointment(@RequestParam Integer aid,
                                  @RequestParam Integer uid,
                                  @RequestParam Integer cid,
                                  @RequestParam String date,
                                  @RequestParam String startTime,
                                  @RequestParam String endTime) throws ParseException {
        return appointmentService.edit(aid, uid, cid, date, startTime, endTime);
    }

}
