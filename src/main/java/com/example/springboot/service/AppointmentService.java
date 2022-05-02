package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Appointment;
import com.example.springboot.mapper.AppointmentMapper;
import org.springframework.stereotype.Service;

import java.awt.*;
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

        //将传入的日期、时间字符串转化为Date类型
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date d = format.parse(date.replace("Z", " UTC"));
        System.out.println(df.format(d));
        startTime = df.format(d) + " " + startTime;
        endTime = df.format(d) + " " + endTime;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        System.out.println("An Output from AppointmentController: date = " + df.format(d) + ", startTime = " + startTime + ", endTime = " + endTime + ".");

        //将已有的参数全部赋给新Appointment对象
        Appointment newA = new Appointment();
        newA.setUId(uid);
        newA.setCId(cid);
        newA.setAStartTime(df.parse(startTime));
        newA.setAEndTime(df.parse(endTime));

        //查询出目前所有本教室的预约
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("c_id", cid);
        List<Appointment> appointmentList = list(queryWrapper);
        System.out.println("The appointments we got are : " + appointmentList);

        for (Appointment oldA : appointmentList) {
//            System.out.println("newA.getAStartTime = " + newA.getAStartTime());
//            System.out.println("oldA.getAStartTime = " + oldA.getAStartTime());
//            System.out.println("newA.getAStartTime.compareTo(oldA.getAStartTime) = " + newA.getAStartTime().compareTo(oldA.getAStartTime()));
            if ((newA.getAStartTime().compareTo(oldA.getAStartTime()) >= 0 && newA.getAStartTime().compareTo(oldA.getAEndTime()) <= 0)
            || (newA.getAEndTime().compareTo(oldA.getAStartTime()) >= 0 && newA.getAEndTime().compareTo(oldA.getAEndTime()) <= 0)) {
                return Result.error(Constants.CODE_500, "预约时间与其他预约冲突");
            }
        }
        if (save(newA))
            return Result.success();
        return Result.error(Constants.CODE_500, "系统错误(新建预约失败)");
    }
}
