package com.novedu.nov.order.client;

import com.novedu.nov.common.base.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author ：juam
 * @date ：2022/2/8 9:58
 * @description：
 * @modified By：
 * @version:
 */
@Component
@FeignClient(name = "service-ucenter")
public interface OpenUcenterService {

    @ApiOperation("根据id获取member")
    @PostMapping("/ucenter/member/info/{id}/whi")
    BaseResult getMemberInfo(@PathVariable("id")Long id);

    @ApiOperation("删除")
    @PostMapping("/ucenter/role/by-uid/{uid}/whi")
    BaseResult queryUserRole(@PathVariable Long uid) ;
}
