package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Classroom;
import com.example.springboot.mapper.ClassroomMapper;
import org.springframework.stereotype.Service;

@Service
public class ClassroomService extends ServiceImpl<ClassroomMapper, Classroom> {

    public boolean saveClassroom(Classroom classroom) {
        return saveOrUpdate(classroom);
    }
}
