package com.example.tmall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan("com.example.tmall.mgb.mapper")
public class MyBatisConfig {

}
