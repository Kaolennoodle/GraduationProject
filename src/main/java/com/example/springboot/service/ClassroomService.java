package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Appointment;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.ClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassroomService extends ServiceImpl<ClassroomMapper, Classroom> {

    @Autowired
    UserService userService;

    @Autowired
    AppointmentService appointmentService;
    public boolean saveClassroom(Classroom classroom) {
        return saveOrUpdate(classroom);
    }

    /**
     * 通过教室id查询管理员信息
     * @param c_id
     * @return
     */
    public User getAdminByCid(Integer c_id) {
        return userService.getById(getById(c_id).getCAdminId());
    }

    public float getActiveRatio() {
        QueryWrapper<Appointment> appointmentQueryWrapper = new QueryWrapper<>();
        appointmentQueryWrapper.between("a_status", 1, 2);
        List<Appointment> appointmentList = appointmentService.list(appointmentQueryWrapper);
        List<Integer> classroomIds = new ArrayList<>();
        for (Appointment appointment: appointmentList) {
            if (classroomIds.size() == 0) {
                classroomIds.add(appointment.getCId());
            }
            for (Integer i: classroomIds) {
                if (!appointment.getCId().equals(i)) {
                    classroomIds.add(appointment.getCId());
                    break;
                }
            }
        }
        float total = list().size();
        int ratio = (int) (classroomIds.size() / total * 100);
        return ratio;
    }
}
