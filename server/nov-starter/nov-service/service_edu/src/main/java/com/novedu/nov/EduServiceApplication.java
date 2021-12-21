package com.novedu.nov;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author ：juam
 * @date ：2021/12/8 13:26
 * @description：
 * @modified By：
 * @version:
 */
@EnableTransactionManagement
@MapperScan(basePackages = "com.novedu.nov.edu.mapper")
@SpringBootApplication(scanBasePackages = "com.novedu.nov")
public class EduServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduServiceApplication.class, args);
    }
}
