package com.novedu.nov.order.client;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.order.entity.EduCourseApply;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ：juam
 * @date ：2022/2/8 10:07
 * @description：
 * @modified By：
 * @version:
 */
@Component
@FeignClient(name = "service-edu")
public interface EduClient {

    @ApiOperation("根据id获取course")
    @PostMapping("/edu/course/detail/{id}")
    BaseResult queryCourseDetail(@PathVariable("id")Long id);

    @PostMapping("/edu/course-apply/save")
    BaseResult saveApply(@RequestBody EduCourseApply courseApply);

    @PostMapping("/edu/course-apply/already")
    BaseResult queryCourseApplyByCourseIdAndUid(@RequestBody EduCourseApply courseApply);

    @ApiOperation("统计课程报名人数")
    @GetMapping("/edu/course/statistics/course/applyCount")
     BaseResult statisticsCourseApplyCount();

    @ApiOperation("统计课程购买量")
    @GetMapping("/edu/course/statistics/course/buyCount")
     BaseResult statisticsCourseBuyCount();

}
