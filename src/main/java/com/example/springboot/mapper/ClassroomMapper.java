package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Classroom;

import java.util.List;

//@Mapper

public interface ClassroomMapper extends BaseMapper<Classroom> {

    /*@Select("select * from classroom")
    List<Classroom> findAll();

    @Insert("insert into classroom(c_name, c_building, c_floor, c_address, c_admin_id) "
            + "values (#{c_name}, #{c_volume}, #{c_building}, #{c_floor}, #{c_address}, #{c_admin_id})")
    int insert(Classroom classroom);

    //    @Update("UPDATE classroom set c_name = #{c_name}, c_building = #{c_building}, c_floor = #{c_floor}, " +
    //            "c_address = #{c_address}, c_admin_id = #{c_admin_id} where c_id = #{c_id}")
    int update(Classroom classroom);

    @Delete("delete from classroom where c_id = #{c_id}")
    Integer deleteById(@Param("c_id") Integer c_id);

    @Select("select * from classroom limit #{pageNum}, #{pageSize}")
    List<Classroom> selectPage(Integer pageNum, Integer pageSize);

    @Select("select count(*) from classroom")
    Integer selectTotal();*/
}
