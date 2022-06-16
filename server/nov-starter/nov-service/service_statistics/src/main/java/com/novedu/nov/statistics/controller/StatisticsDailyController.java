package com.novedu.nov.statistics.controller;


import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.statistics.service.StatisticsDailyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-02-09
 */
@RestController
@RequestMapping("/statistics/daily")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    @ApiOperation("统计最近一周用户注册和访问数")
    @GetMapping("/register-and-access")
    public BaseResult statisticsAWeekUserRegisterAndLoginCount(){
        return statisticsDailyService.statisticsAWeekUserRegisterAndLoginCount();
    }

}

