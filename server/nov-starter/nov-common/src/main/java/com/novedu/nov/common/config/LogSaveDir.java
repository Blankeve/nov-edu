package com.novedu.nov.common.config;

import ch.qos.logback.core.PropertyDefinerBase;
import org.springframework.stereotype.Component;

/**
 * @author ：juam
 * @date ：2021/12/8 16:41
 * @description：
 * @modified By：
 * @version:
 */
@Component
public class LogSaveDir extends PropertyDefinerBase  {

    @Override
    public String getPropertyValue() {
        System.out.println("PropertyDefiner Start...");
        String logDir="";
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("os:"+os);
        if(os.indexOf("win")!=-1){
            String projDir = System.getProperty("user.dir");
            String user = System.getenv("USERNAME");
            logDir = String.format("%s/log",projDir);
        }
        else if(os.indexOf("lin")!=-1){
            logDir = "/usr/local/java/nov_log";
        }
        System.out.println("logDir:"+logDir);
        return logDir;
    }

}
