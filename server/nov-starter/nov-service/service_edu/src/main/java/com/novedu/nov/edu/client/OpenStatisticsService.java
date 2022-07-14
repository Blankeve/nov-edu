package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "service-statistics")
public interface OpenStatisticsService {

    @ApiOperation("统计最近一周用户注册和访问数")
    @GetMapping("/statistics/daily/register-and-access")
    BaseResult statisticsAWeekUserRegisterAndAccessCount();
}
