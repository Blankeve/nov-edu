package com.novedu.nov.edu.client;

import com.novedu.nov.common.api.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Component
@FeignClient(value = "service-order")
public interface OrderClient {

    @ApiOperation("查询用户是否已经下单")
    @PostMapping("/order/trade/hasbuy/{id}")
    BaseResult queryOrderByUidAndCourseId(@PathVariable Long id);


    @ApiOperation("查询成交订单数量")
    @PostMapping("/order/trade/count/{teacherId}")
    BaseResult queryOrderCount(@PathVariable Long teacherId);
}
