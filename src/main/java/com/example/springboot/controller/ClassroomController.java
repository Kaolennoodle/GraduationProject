package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Classroom;
import com.example.springboot.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    /**
     * 新增或编辑
     *
     * @param classroom
     * @return
     */
    @PostMapping
    public boolean save(@RequestBody Classroom classroom) { //新增或更新
        return classroomService.saveClassroom(classroom);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    @GetMapping
    public List<Classroom> findAll() {
        List<Classroom> list = classroomService.list();
        System.out.println(list);
        return list;
    }

    /**
     * 按c_id删除
     *
     * @param c_id
     * @return
     */
    @DeleteMapping("/{c_id}")
    public boolean delete(@PathVariable Integer c_id) {
        return classroomService.removeById(c_id);
    }

    /**
     * 批量删除
     *
     * @param c_ids
     * @return
     */
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> c_ids) {
        return classroomService.removeBatchByIds(c_ids);
    }

    /**
     * 分页查询-MyBatis-Plus
     *
     * @param pageNum
     * @param pageSize
     * @param c_name
     * @param c_volume
     * @param c_building
     * @param c_floor
     * @return
     */
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
        if (c_volume != null)
            queryWrapper.eq("c_volume", c_volume);
        if (c_building != null)
            queryWrapper.eq("c_building", c_building);
        if (c_floor != null)
            queryWrapper.eq("c_floor", c_floor);
        queryWrapper.orderByDesc("c_create_time");
        return classroomService.page(page, queryWrapper);
    }
}
