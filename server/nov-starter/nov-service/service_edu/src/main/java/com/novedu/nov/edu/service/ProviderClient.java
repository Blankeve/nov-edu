package com.novedu.nov.edu.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ：juam
 * @date ：2022/1/10 15:58
 * @description：
 * @modified By：
 * @version:
 */
@FeignClient(value = "upload-service")
public interface ProviderClient {
    @GetMapping("/service")
    String service();
}
