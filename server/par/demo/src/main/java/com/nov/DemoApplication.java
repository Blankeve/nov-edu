package com.nov;

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
@MapperScan(basePackages = "com.nov.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
    }
}
