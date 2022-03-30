package com.example.springboot;

import com.example.springboot.entity.Classroom;
import com.example.springboot.mapper.ClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class SpringbootApplication {



    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }


//    @GetMapping("/")
//    public String index() {
//        return "ok";
//    }
}
