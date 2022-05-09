package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Appointment;
import com.example.springboot.mapper.AppointmentMapper;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService extends ServiceImpl<AppointmentMapper, Appointment> {

    /**
     * 新增预约
     *
     * @param uid
     * @param cid
     * @param date
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public Result makeNew(Integer uid, Integer cid, String date, String startTime, String endTime) throws ParseException {

        Appointment newA = constructor(null, uid, cid, date, startTime, endTime);

        Date now = new Date();
        if (newA.getAStartTime().compareTo(now) <= 0)
            return Result.error(Constants.CODE_500, "预约开始时间不能早于当前时间");
        if (newA.getAStartTime().compareTo(newA.getAEndTime()) >= 0)
            return Result.error(Constants.CODE_500, "预约结束时间不能早于预约开始时间");
        if (validate(newA)) {
            if (save(newA)) ;
            return Result.success();
        } else
            return Result.error(Constants.CODE_500, "预约时间与已有预约冲突");
    }

    /**
     * 将前台传回的字符串信息封装为Appointment类型
     *
     * @return 封装好的appointment对象
     */
    public Appointment constructor(Integer aid, Integer uid, Integer cid, String date, String startTime, String endTime) throws ParseException {

        System.out.println("/n/n/n");
        System.out.println("String startTime = " + startTime + ", String endTime = " + endTime + " And String Date = " + date);

        //将传入的日期、时间字符串转化为Date类型
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

        Date dateDate = format.parse(date.replace("Z", " UTC"));

        System.out.println("dateDate = " + dateDate);

        Date startTimeDate = null;
        Date endTimeDate = null;
        if (startTime != "")
            startTimeDate = format.parse(startTime.replace("Z", " UTC"));
        else
            startTimeDate = getById(aid).getAStartTime();

        if (endTime != "")
            endTimeDate = format.parse(endTime.replace("Z", " UTC"));
        else
            endTimeDate = getById(aid).getAEndTime();

        startTimeDate.setDate(dateDate.getDate());
        endTimeDate.setDate(dateDate.getDate());

        //将已有的参数全部赋给新Appointment对象
        Appointment appointment = new Appointment(aid, cid, uid, startTimeDate, endTimeDate, 1);

        System.out.println("Appointment: startTimeDate = " + appointment.getAStartTime() + " And endTimeDate = " + appointment.getAEndTime());
        System.out.println("/n/n/n");
        return appointment;
    }

    /**
     * 修改预约信息
     *
     * @param aid
     * @param uid
     * @param cid
     * @param date
     * @param startTime
     * @param endTime
     * @return Result对象
     */
    public Result edit(Integer aid, Integer uid, Integer cid, String date, String startTime, String endTime) throws ParseException {
        Appointment newA = constructor(aid, uid, cid, date, startTime, endTime);
        Date now = new Date();
        if (newA.getAStartTime().compareTo(now) <= 0)
            return Result.error(Constants.CODE_500, "预约开始时间不能早于当前时间");
        if (newA.getAStartTime().compareTo(newA.getAEndTime()) >= 0)
            return Result.error(Constants.CODE_500, "预约结束时间不能早于预约开始时间");
        if (validate(newA))
            saveOrUpdate(newA);
        else
            return Result.error(Constants.CODE_500, "预约时间与已有预约冲突");
        return Result.success();
    }

    /**
     * 检验预约是否跟已有预约时间冲突
     * 若传入的appointment含有aid，则在校验时会跳过数据库中aid字段相同的预约（更新）
     *
     * @param appointment
     * @return 布尔变量，true为合法，false为非法
     */
    public boolean validate(Appointment appointment) {

        //查询出目前所有本教室的预约
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("c_id", appointment.getCId());
        List<Appointment> appointmentList = list(queryWrapper);

        //如果当前教室无任何预约信息，则跳过验证直接成功
        if (appointmentList.toArray().length == 0)
            return true;

        //若查询出当前教室存在预约信息，则逐条验证时间是否冲突
        int i1, i2, i3, i4;
        for (Appointment oldA : appointmentList) {
            i1 = appointment.getAStartTime().compareTo(oldA.getAStartTime());
            i2 = appointment.getAStartTime().compareTo(oldA.getAEndTime());
            i3 = appointment.getAEndTime().compareTo(oldA.getAStartTime());
            i4 = appointment.getAEndTime().compareTo(oldA.getAEndTime());

            if ((i1 < 0 && i3 < 0) || (i2 > 0 && i4 > 0) || (appointment.getAId() != null && appointment.getAId() == oldA.getAId())) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
