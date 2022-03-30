package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Classroom;
import com.example.springboot.mapper.ClassroomMapper;
import com.example.springboot.service.ClassroomService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @PostMapping
    public boolean save(@RequestBody Classroom classroom) { //新增或更新
        return classroomService.save(classroom);
    }

    //查询所有数据
    @GetMapping
    public List<Classroom> findAll() {
        List<Classroom> list = classroomService.list();
        System.out.println(list);
        return list;
    }

    //按c_id删除
    @DeleteMapping("/{c_id}")
    public boolean delete(@PathVariable Integer c_id) {
        return classroomService.removeById(c_id);
    }

    //分页查询-MyBatis-Plus
    @GetMapping("/page")
    public IPage<Classroom> findPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String c_name,
                                     @RequestParam(defaultValue = "") Integer c_volume,
                                     @RequestParam(defaultValue = "") Integer c_building,
                                     @RequestParam(defaultValue = "") Integer c_floor) {
        IPage<Classroom> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();

        //模糊查询
        queryWrapper.like("c_name", c_name);
        if(c_volume != null)
            queryWrapper.like("c_volume", c_volume);
        if (c_building != null)
            queryWrapper.like("c_building", c_building);
        if (c_floor != null)
            queryWrapper.like("c_floor", c_floor);
        return classroomService.page(page, queryWrapper);
    }
    /*@GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        pageNum = (pageNum - 1) * pageSize;
        List<Classroom> data = classroomMapper.selectPage(pageNum, pageSize);
        Integer total = classroomMapper.selectTotal();
        System.out.println(data);
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("total", total);
        return res;
    }*/
}
