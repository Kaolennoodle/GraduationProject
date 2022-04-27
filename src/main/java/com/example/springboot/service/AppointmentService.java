package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Appointment;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.AppointmentMapper;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService extends ServiceImpl<AppointmentMapper, Appointment> {

    /**
     * 新增预约
     * @param appointment
     * @return
     */
    public boolean makeNew(Appointment appointment) {
        return save(appointment);
    }
}
