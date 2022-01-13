package com.novedu.nov;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author ：juam
 * @date ：2021/12/8 13:26
 * @description：
 * @modified By：
 * @version:
 */
@EnableTransactionManagement
@MapperScan(basePackages = "com.novedu.nov.cms.mapper")
@SpringBootApplication(scanBasePackages = "com.novedu.nov")
@EnableDiscoveryClient
@EnableFeignClients
public class CmsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsServiceApplication.class, args);
    }
}
