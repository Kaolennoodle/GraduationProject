package com.example.springboot.controller;

import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Appointment;
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

    /**
     * 通用分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param u_id
     * @param u_name
     * @param c_name
     * @param a_date
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
                           @RequestParam(defaultValue = "") String a_start_time,
                           @RequestParam(defaultValue = "") String a_end_time) {
        return appointmentService.findPage(pageNum,pageSize,u_id,c_admin_id,a_approval_status,u_name,c_name,a_date, a_start_time,a_end_time);
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
