package com.nov.edu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ：juam
 * @date ：2021/12/7 16:51
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootApplication
@MapperScan(basePackages = "com.nov.edu.mapper")
public class EduServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduServiceApplication.class,args);
    }
}
