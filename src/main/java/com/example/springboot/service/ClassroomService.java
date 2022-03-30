package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Classroom;
import com.example.springboot.mapper.ClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassroomService extends ServiceImpl<ClassroomMapper, Classroom> {

    public boolean saveClassroom(Classroom classroom) {
        return saveOrUpdate(classroom);
    }
    /*@Autowired
    private ClassroomMapper classroomMapper;
    public int save(Classroom classroom) {
        if(classroom.getC_id() == null) { //没有id，表示为新增，
            return classroomMapper.insert(classroom);
        } else { //否则为更新
            return classroomMapper.update(classroom);
        }
    }*/
}
