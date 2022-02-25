package com.novedu.nov.edu.client;

import com.novedu.nov.common.api.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(value = "service-ucenter")
public interface UserRoleClient {

    @ApiOperation("删除")
    @PostMapping("/ucenter/role/by-uid/{uid}")
    BaseResult queryUserRole(@PathVariable Long uid) ;
}
