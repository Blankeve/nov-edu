package com.novedu.nov.system.config;

import com.novedu.nov.common.helper.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：juam
 * @date ：2021/12/15 9:29
 * @description：
 * @modified By：
 * @version:
 */
@Configuration
public class NovSnowFlakeConfig {

    private Long machineId = 1l;
    private Long dataCenterId = 1l;

    @Bean
    public SnowFlake snowFlakeUtils(){
        return new SnowFlake(machineId,dataCenterId);
    }
}
