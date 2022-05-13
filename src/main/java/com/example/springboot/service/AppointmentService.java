package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Appointment;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService extends ServiceImpl<AppointmentMapper, Appointment> {

    @Autowired
    ClassroomService classroomService;
    @Autowired
    UserService userService;

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

        //将传入的日期、时间字符串转化为Date类型
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

        Date dateDate;
        try {
            dateDate = format.parse(date.replace("Z", " UTC"));
        } catch (Exception e) {
            dateDate = df.parse(date.replace("Z", " UTC"));
        }

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
        Appointment appointment = new Appointment(aid, cid, uid, startTimeDate, endTimeDate, 1, null);
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

//        仅查询待审核和已通过的预约
        queryWrapper.between("a_approval_status", 1, 2);

//        仅查询尚未过期的预约
        queryWrapper.between("a_status", 1, 2);
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

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param u_id
     * @param c_admin_id
     * @param a_approval_status
     * @param u_name
     * @param c_name
     * @param a_date
     * @param a_start_time
     * @param a_end_time
     * @return
     */
    public Result findPage(Integer pageNum,
                           Integer pageSize,
                           Integer u_id,
                           Integer c_admin_id,
                           Integer a_approval_status,
                           String u_name,
                           String c_name,
                           String a_date,
                           String a_start_time,
                           String a_end_time) {


//        System.out.println("a_date = " + a_date);
//        System.out.println("a_start_time = " + a_start_time);
//        System.out.println("a_end_time = " + a_end_time);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        Date date = null;
        Date startTime = null;
        Date endTime = null;

        try {

            if (!a_date.equals("")) {
                date = dateFormat.parse(a_date);
            }

            if (!a_start_time.equals("")) {
                startTime = timeFormat.parse(a_start_time);
            }

            if (!a_end_time.equals("")) {
                endTime = timeFormat.parse(a_end_time);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("a_date = " + date);
        System.out.println("a_start_time = " + startTime);
        System.out.println("a_end_time = " + endTime);

/**
 * 接下来的if-else代码块将根据传入的日期、开始时间和结束时间拼装成合理、便于数据库查询的带日期的开始时间与结束时间
 */
//        如果日期不为空
        if (date != null) {
//            开始时间不为空
            if (startTime != null) {
//                则把开始时间的日期设为传入的日期
                setFullDate(date, startTime);
//                如果结束时间也不为空
                if (endTime != null) {
//                    就把结束时间的日期也设为传入的日期
                    setFullDate(date, endTime);
//                    如果结束时间为空，
                } else {
//                    那就设为跟开始时间一样
                    endTime = startTime;
                }
//                如果开始时间为空
            } else {
//                且结束时间不为空
                if (endTime != null) {
//                    那就把结束时间的日期设为传入的日期
                    setFullDate(date, endTime);
//                    同时把开始时间设为跟结束时间相同
                    startTime = endTime;
//                    如果开始时间和结束时间都为空，则直接将日期赋予
                } else {
                    startTime = new Date();
                    endTime = new Date();

                    startTime.setTime(date.getTime());
                    endTime.setTime(date.getTime());
//                        为了避免查不到预约，我们把赋予了日期了的时间调整至覆盖全天的预约
                    startTime.setHours(7);
                    endTime.setHours(23);
                }
            }
//            如果传入的日期为空
        } else {
//            但传入的开始时间不为空
            if (startTime != null) {
//                则将当前时间的日期赋给开始时间
                setFullDate(new Date(), startTime);
//                如果结束时间不为空
                if (endTime != null) {
//                    就将当前时间的日期赋给结束时间
                    setFullDate(new Date(), endTime);
//                    如果结束时间为空
                } else {
//                    就令结束时间跟开始时间相同
                    endTime = startTime;
                }
//                如果开始时间为空
            } else {
//                且结束时间不为空
                if (endTime != null) {
//                    则将当前时间的日期赋给结束时间
                    setFullDate(new Date(), endTime);
//                    并令开始时间等于结束时间
                    startTime = endTime;
                }
            }
        }

        System.out.println("After transform:");
        System.out.println("a_start_time = " + startTime);
        System.out.println("a_end_time = " + endTime);

        IPage<Appointment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Appointment> appointmentQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Classroom> classroomQueryWrapper = new QueryWrapper<>();

        classroomQueryWrapper.select("c_id");
        userQueryWrapper.select("u_id");

        List<User> userList;
        List<Integer> u_ids = new ArrayList<>();
        List<Classroom> classroomList;
        List<Integer> c_ids = new ArrayList<>();
//        如果传入了开始时间和结束时间，则加入筛选条件
        if (startTime != null && endTime != null) {
            List<Appointment> allAppts = new ArrayList<>();
            List<Appointment> qualifiedApptList = new ArrayList<>();
            List<Integer> qualifiedApptIdList = new ArrayList<>();
            System.out.println("当前查询的开始时间为：" + startTime + " 结束时间为：" + endTime);
            allAppts = list();
            qualifiedApptList = allAppts;
            System.out.println(allAppts);
            int i1, i2, i3, i4;
//            for(int i=list.size()-1;i>=0;i--){
//                list.remove(i);
//            }
            for (int i = allAppts.size() - 1; i >= 0; i --) {
                i1 = startTime.compareTo(allAppts.get(i).getAStartTime());
                i2 = startTime.compareTo(allAppts.get(i).getAEndTime());
                i3 = endTime.compareTo(allAppts.get(i).getAStartTime());
                i4 = endTime.compareTo(allAppts.get(i).getAEndTime());

                if ((i1 < 0 && i3 < 0) || (i2 > 0 && i4 > 0)) {
                    System.out.println("id为" + allAppts.get(i).getAId() + "的预约不符合要求，因为其开始时间为：" + allAppts.get(i).getAStartTime() + " 结束时间为：" + allAppts.get(i).getAEndTime());
                    qualifiedApptList.remove(allAppts.get(i));
                }
            }

            if (qualifiedApptList.size() != 0) {
                for (Appointment qualifiedAppt: qualifiedApptList) {
                    qualifiedApptIdList.add(qualifiedAppt.getAId());
                }

                System.out.println("符合要求的预约id有：" + qualifiedApptIdList);
                appointmentQueryWrapper.in("a_id", qualifiedApptIdList);
            } else {
                appointmentQueryWrapper.eq("a_id", -1);
            }

        }

//        如果传入了用户id，则说明是用户在调用此接口，结果仅返回此用户创建的预约
        if (u_id != null)
            appointmentQueryWrapper.eq("u_id", u_id);

//        如果传入了教室管理员id，则说明是教室管理员在调用此接口，结果仅返回预约教室为管理员所管理的教室的预约
//        System.out.println("c_admin_id = " + c_admin_id);
        if (c_admin_id != null) {
            classroomQueryWrapper.eq("c_admin_id", c_admin_id);
            classroomList = classroomService.list(classroomQueryWrapper);
            if (classroomList.size() == 0)
                return Result.error(Constants.CODE_500, "您没有正在管理的教室，请联系系统管理员");

            c_ids = new ArrayList<>();
            for (Classroom classroom : classroomList) {
                c_ids.add(classroom.getCId());
            }

            appointmentQueryWrapper.in("c_id", c_ids);
        }

//        如果传入了审核状态，那么仅返回符合条件的预约
        if (a_approval_status != null) {
            appointmentQueryWrapper.eq("a_approval_status", a_approval_status);
        }

        if (u_name.length() > 0) {

            //在用户表中读出相关用户的u_id
            userQueryWrapper.like("u_name", u_name);
            userList = userService.list(userQueryWrapper);

            //如果查询不到则返回错误
            if (userList.size() == 0)
                return Result.error(Constants.CODE_500, "结果中不存在姓名类似“" + u_name + "”的用户");

            //查询到u_id后将查询到的u_id放入u_ids中
            u_ids = new ArrayList<>();
            for (User user : userList) {
                u_ids.add(user.getUId());
            }

            //使用QueryWapper.in查询所有传入用户相关的预约信息
            appointmentQueryWrapper.in("u_id", u_ids);
        }

        //教室查询逻辑跟用户查询类似
        if (c_name.length() > 0) {
            classroomQueryWrapper.like("c_name", c_name);
            classroomList = classroomService.list(classroomQueryWrapper);

            if (classroomList.size() == 0)
                return Result.error(Constants.CODE_500, "结果中不存在名称类似“" + c_name + "”的教室");

            c_ids = new ArrayList<>();
            for (Classroom classroom : classroomList) {
                c_ids.add(classroom.getCId());
            }

            appointmentQueryWrapper.in("c_id", c_ids);
        }

        //全部查询结束后，结果按照预约开始时间排序
        appointmentQueryWrapper.orderByDesc("a_create_time");
        return Result.success(page(page, appointmentQueryWrapper));
    }
    public void setFullDate(Date originalDate, Date targetDate) {
        targetDate.setYear(originalDate.getYear());
        targetDate.setMonth(originalDate.getMonth());
        targetDate.setDate(originalDate.getDate());
    }

    public Date setFullTime(Date originalDate, Date targetDate) {
        targetDate.setHours(originalDate.getHours());
        targetDate.setMinutes(originalDate.getMinutes());
        targetDate.setSeconds(originalDate.getSeconds());
        return targetDate;
    }
}
