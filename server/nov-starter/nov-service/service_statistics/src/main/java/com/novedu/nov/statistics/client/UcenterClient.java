package com.novedu.nov.statistics.client;

import com.novedu.nov.common.base.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author ：juam
 * @date ：2022/2/8 10:07
 * @description：
 * @modified By：
 * @version:
 */
@Component
@FeignClient(name = "service-ucenter")
public interface UcenterClient {

    @ApiOperation("同步每天用户新增注册和登录人数")
    @GetMapping("/ucenter/member/sync-register-login")
    BaseResult syncRegisterLoginCount();
}
