package com.novedu.nov;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author ：juam
 * @date ：2021/12/8 13:26
 * @description：
 * @modified By：
 * @version:
 */
@EnableTransactionManagement
@MapperScan(basePackages = {"com.novedu.nov.statistics.mapper","com.novedu.nov.common.module.mapper"})
@SpringBootApplication(scanBasePackages = "com.novedu.nov")
@EnableDiscoveryClient
@EnableFeignClients
@EnableSwagger2
public class StatisticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsServiceApplication.class, args);
    }
}
