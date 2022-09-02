package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(value = "service-order")
public interface OpenOrderService {

    @ApiOperation("查询用户是否已经下单")
    @PostMapping("/order/trade/hasbuy/{id}/{uid}")
    BaseResult queryOrderByUidAndCourseId(@PathVariable Long id, @PathVariable Long uid);


    @ApiOperation("查询成交订单数量")
    @PostMapping("/order/trade/count/{teacherId}/whi")
    BaseResult queryOrderCount(@PathVariable Long teacherId);
}
