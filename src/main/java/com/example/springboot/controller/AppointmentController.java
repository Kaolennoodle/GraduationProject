package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
            userQueryWrapper.like("u_name", u_name);
            userList = userService.list(userQueryWrapper);
            for (User user : userList) {
                appointmentQueryWrapper.eq("u_id", user.getUId());
            }
        }
        if (c_name.length() > 0) {
            classroomQueryWrapper.like("c_name", c_name);
            classroomList = classroomService.list(classroomQueryWrapper);
            for (Classroom classroom : classroomList) {
                appointmentQueryWrapper.eq("c_id", classroom.getCId());
            }
        }
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
