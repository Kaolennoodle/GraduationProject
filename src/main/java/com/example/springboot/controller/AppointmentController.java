package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Appointment;
import com.example.springboot.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

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

    @GetMapping("/page")
    public IPage<Appointment> findPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") Integer u_id,
                                       @RequestParam(defaultValue = "") Integer c_id,
                                       @RequestParam(defaultValue = "") String a_date,
                                       @RequestParam(defaultValue = "") String a_time,
                                       @RequestParam(defaultValue = "") String a_start_time,
                                       @RequestParam(defaultValue = "") String a_end_time) {

        IPage<Appointment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();

        if (u_id != null)
            queryWrapper.eq("u_id", u_id);
        if (c_id != null)
            queryWrapper.eq("c_id", c_id);

        return appointmentService.page(page, queryWrapper);
    }
}
