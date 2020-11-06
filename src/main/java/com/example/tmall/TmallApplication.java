package com.example.tmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@MapperScan({"com.example.tmall.dao", "com.example.tmall.mbg.mapper"})
public class TmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmallApplication.class, args);
    }

}
