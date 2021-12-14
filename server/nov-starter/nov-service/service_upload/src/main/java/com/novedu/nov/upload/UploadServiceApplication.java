package com.novedu.nov.upload;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


/**
 * @author ：juam
 * @date ：2021/12/8 13:26
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootApplication(scanBasePackages = "com.novedu.nov",exclude = DataSourceAutoConfiguration.class)
public class UploadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadServiceApplication.class, args);
    }
}
