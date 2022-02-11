package com.novedu.nov.common.config;

import com.novedu.nov.common.module.entity.SysConfig;
import com.novedu.nov.common.module.service.SysConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author ：juam
 * @date ：2022/2/9 11:14
 * @description：
 * @modified By：
 * @version:
 */
@Component
public class SysConfigCache implements InitializingBean {

    private static List<SysConfig> SYS_CONFIG;

    @Autowired
    SysConfigService configService;

    @Override
    public void afterPropertiesSet()  {
        SYS_CONFIG =  configService.list();
        System.out.println("获取配置项个数:"+SYS_CONFIG.size());
    }

    public static SysConfig getConfigByKey(String key){
      return SYS_CONFIG.stream().filter(o->o.getConfigKey().equals(key)).findAny().orElse(null);
    }
}
