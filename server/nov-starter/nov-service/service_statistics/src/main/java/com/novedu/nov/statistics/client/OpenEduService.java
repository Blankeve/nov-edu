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
@FeignClient(name = "service-edu")
public interface OpenEduService {

    @ApiOperation("统计课程播放量")
    @GetMapping("/edu/course/statistics/course/playCount/whi")
    BaseResult statisticsCoursePlayCount();

    @ApiOperation("统计课程报名人数")
    @GetMapping("/edu/course/statistics/course/applyCount/whi")
    BaseResult statisticsCourseApplyCount();

    @ApiOperation("统计课程购买量")
    @GetMapping("/edu/course/statistics/course/buyCount/whi")
    BaseResult statisticsCourseBuyCount();
}
