package com.novedu.nov.ucenter.client;

import com.novedu.nov.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * @author ：juam
 * @date ：2022/2/8 10:07
 * @description：
 * @modified By：
 * @version:
 */
@Component
@FeignClient(name = "nov-auth")
public interface OpenAuthService {

    @PostMapping("/oauth/token")
    BaseResult postAccessToken(@RequestParam Map<String, String> parameters);
}

