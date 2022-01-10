package com.novedu.nov.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：juam
 * @date ：2022/1/10 15:40
 * @description：
 * @modified By：
 * @version:
 */
@RestController
@Slf4j
public class ApiTestController {

    @GetMapping("/service")
    public String service(){
        log.info("provider invoke");
        return "provider invoke";
    }

}
