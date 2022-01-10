package com.novedu.nov.upload.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ：juam
 * @date ：2022/1/10 15:58
 * @description：
 * @modified By：
 * @version:
 */
@FeignClient(value = "quickstart-provider")
public interface ProviderClient {
    @GetMapping("/service")
    String service();
}
