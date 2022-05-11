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
     * 通用分页查询
     *
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
                           @RequestParam(defaultValue = "") Integer c_admin_id,
                           @RequestParam(defaultValue = "") Integer a_approval_status,
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

        classroomQueryWrapper.select("c_id");
        userQueryWrapper.select("u_id");

        List<User> userList;
        List<Integer> u_ids = new ArrayList<>();
        List<Classroom> classroomList;
        List<Integer> c_ids = new ArrayList<>();

//        如果传入了用户id，则说明是用户在调用此接口，结果仅返回此用户创建的预约
        if (u_id != null)
            appointmentQueryWrapper.eq("u_id", u_id);

//        如果传入了教室管理员id，则说明是教室管理员在调用此接口，结果仅返回预约教室为管理员所管理的教室的预约
        System.out.println("c_admin_id = " + c_admin_id);
        if (c_admin_id != null) {
            classroomQueryWrapper.eq("c_admin_id", c_admin_id);
            classroomList = classroomService.list(classroomQueryWrapper);
            if (classroomList.size() == 0)
                return Result.error(Constants.CODE_500, "您没有正在管理的教室，请联系系统管理员");

            c_ids = new ArrayList<>();
            for (Classroom classroom : classroomList) {
                c_ids.add(classroom.getCId());
            }

            appointmentQueryWrapper.in("c_id", c_ids);
        }

//        如果传入了审核状态，那么仅返回符合条件的预约
        if (a_approval_status != null) {
            appointmentQueryWrapper.eq("a_approval_status", a_approval_status);
        }

        if (u_name.length() > 0) {

            //在用户表中读出相关用户的u_id
            userQueryWrapper.like("u_name", u_name);
            userList = userService.list(userQueryWrapper);

            //如果查询不到则返回错误
            if (userList.size() == 0)
                return Result.error(Constants.CODE_500, "结果中不存在姓名类似“" + u_name + "”的用户");

            //查询到u_id后将查询到的u_id放入u_ids中
            u_ids = new ArrayList<>();
            for (User user : userList) {
                u_ids.add(user.getUId());
            }

            //使用QueryWapper.in查询所有传入用户相关的预约信息
            appointmentQueryWrapper.in("u_id", u_ids);
        }

        //教室查询逻辑跟用户查询类似
        if (c_name.length() > 0) {
            classroomQueryWrapper.like("c_name", c_name);
            classroomList = classroomService.list(classroomQueryWrapper);

            if (classroomList.size() == 0)
                return Result.error(Constants.CODE_500, "结果中不存在名称类似“" + c_name + "”的教室");

            c_ids = new ArrayList<>();
            for (Classroom classroom : classroomList) {
                c_ids.add(classroom.getCId());
            }

            appointmentQueryWrapper.in("c_id", c_ids);
        }

        //全部查询结束后，结果按照预约开始时间排序
        appointmentQueryWrapper.orderByAsc("a_start_time");
        return Result.success(appointmentService.page(page, appointmentQueryWrapper));
    }

    /**
     * 更新预约
     *
     * @param aid
     * @param uid
     * @param cid
     * @param date
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    @GetMapping("/edit")
    public Result editAppointment(@RequestParam Integer aid,
                                  @RequestParam Integer uid,
                                  @RequestParam Integer cid,
                                  @RequestParam String date,
                                  @RequestParam String startTime,
                                  @RequestParam String endTime) throws ParseException {
        return appointmentService.edit(aid, uid, cid, date, startTime, endTime);
    }

    /**
     * 批准预约请求
     * @param a_id
     * @return
     */
    @GetMapping("/approve/{a_id}")
    public Result approveAppt(@PathVariable Integer a_id) {
        if (appointmentService.saveOrUpdate(new Appointment(a_id, null, null, null, null, null, 2)))
        return Result.success();
        else return Result.error(Constants.CODE_500, "系统错误(在AppointController中)");
    }

    /**
     * 拒绝预约请求
     * @param a_id
     * @return
     */
    @GetMapping("/reject/{a_id}")
    public Result rejectAppt(@PathVariable Integer a_id) {
        if (appointmentService.saveOrUpdate(new Appointment(a_id, null, null, null, null, null, 3)))
            return Result.success();
        else return Result.error(Constants.CODE_500, "系统错误(在AppointController中)");
    }

}
