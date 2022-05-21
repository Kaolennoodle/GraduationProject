package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Appointment;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.Message;
import com.example.springboot.entity.User;
import com.example.springboot.service.AppointmentService;
import com.example.springboot.service.ClassroomService;
import com.example.springboot.service.MessageService;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.TokenUtils;
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

    @Autowired
    private MessageService messageService;

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
        return appointmentService.edit(aid, uid, cid, TokenUtils.getCurrentUser(), date, startTime, endTime);
    }

    /**
     * 批准预约请求
     * @param a_id
     * @return
     */
    @GetMapping("/approve/{a_id}")
    public Result approveAppt(@PathVariable Integer a_id) {
        if (appointmentService.saveOrUpdate(new Appointment(a_id, null, null, null, null, null, 2))) {
            Message message = new Message();
            Appointment appointment = appointmentService.getById(a_id);
            User user = userService.getById(appointment.getUId());
            Classroom classroom = classroomService.getById(appointment.getCId());
            User cAdmin = userService.getById(classroom.getCAdminId());
            message.setMTitle("您的预约已获批准！");
            message.setMContent("你在" + classroom.getCName() +
                    "教室，开始于" + appointment.getAStartTime() +
                    "的预约已获批准！请在预约时间内到达预约教室。有问题请及时联系教室管理员：" + cAdmin.getUName() + " " + cAdmin.getUPhone() +
                    " 祝您学习生活愉快！");
            message.setMReceiverId(user.getUId());
            messageService.save(message);
            message.setMId(null);
            message.setMReceiverId(cAdmin.getUId());
            message.setMTitle("您已批准一个预约");
            message.setMContent("您批准了来自" + user.getUName() +
                    "，在您管理的教室（" + classroom.getCName() +
                    "），开始于" + appointment.getAStartTime() +
                    "的预约。在预约进行期间请保持手机畅通，以便用户及时联系。如有特殊情况，请及时通知用户：" + user.getUName() + " " + user.getUPhone() +
                    "。祝您生活愉快！");
            messageService.save(message);
            return Result.success();
        }
        else return Result.error(Constants.CODE_500, "系统错误(在AppointController中)");
    }

    /**
     * 拒绝预约请求
     * @param a_id
     * @return
     */
    @GetMapping("/reject")
    public Result rejectAppt(@RequestParam Integer a_id, @RequestParam String rejectReason) {
        if (appointmentService.saveOrUpdate(new Appointment(a_id, null, null, null, null, null, 3))) {
            Message message = new Message();
            Appointment appointment = appointmentService.getById(a_id);
            User user = userService.getById(appointment.getUId());
            Classroom classroom = classroomService.getById(appointment.getCId());
            User cAdmin = userService.getById(classroom.getCAdminId());
            message.setMTitle("很抱歉，您的预约遭到拒绝。");
            String content = "你在" + classroom.getCName() +
                    "教室，开始于" + appointment.getAStartTime() +
                    "的预约遭到拒绝，";
            if (rejectReason != null) {
                content = content + "原因是：" + rejectReason + "。";
            } else {
                content = content + "管理员未给出具体原因。";
            }
            content = content + "如有疑问，请联系管理员：" + cAdmin.getUName() + " " + cAdmin.getUPhone() + " 或尝试重新申请。祝您学习生活愉快！";
            message.setMContent(content);
            message.setMReceiverId(user.getUId());
            messageService.save(message);
            message.setMId(null);
            message.setMReceiverId(cAdmin.getUId());
            message.setMTitle("您已拒绝一个预约。");
            content = "您拒绝了来自" + user.getUName() +
                    "，在您管理的教室（" + classroom.getCName() +
                    "），开始于" + appointment.getAStartTime() +
                    "的预约";
            if (rejectReason != null) {
                content = content + "，原因是：" + rejectReason + "。";
            } else {
                content = content + "且未给出具体原因。";
            }
            content = content + "如有需要，您可以尝试联系用户说明具体情况：" + user.getUName() + " " + user.getUPhone() +
                    "。祝您生活愉快！";
            message.setMContent(content);
            messageService.save(message);
            return Result.success();
        }
        else return Result.error(Constants.CODE_500, "系统错误(在AppointController中)");
    }

    @GetMapping("/active-num")
    public Result getActiveNum() {
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("a_status", 1, 2);
        return Result.success(appointmentService.list(queryWrapper).size());
    }

    @GetMapping("/all-num")
    public Result getAllNum() {
        return Result.success(appointmentService.list().size());
    }

    @GetMapping("/ocpt-ratio-1")
    public Result getOccupationRatioOn1Day() {
        return Result.success(appointmentService.getOccupationRatio(1));
    }

    @GetMapping("/ocpt-ratio-5")
    public Result getOccupationRatioOn5Days() {
        return Result.success(appointmentService.getOccupationRatio(5));
    }

}
