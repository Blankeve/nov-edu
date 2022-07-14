package com.novedu.nov.upload;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author ：juam
 * @date ：2021/12/8 13:26
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootApplication(scanBasePackages = "com.novedu.nov")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {"com.novedu.nov.system.mapper"})
public class UploadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadServiceApplication.class, args);
        System.out.println("上传服务启动成功");
    }
}
