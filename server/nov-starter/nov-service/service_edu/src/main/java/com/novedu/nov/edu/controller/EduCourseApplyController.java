package com.novedu.nov.edu.controller;


import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduCourseApply;
import com.novedu.nov.edu.service.EduCourseApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程报名 前端控制器
 * </p>
 *
 * @author juam
 * @since 2022-02-11
 */
@RestController
@RequestMapping("/edu/course-apply")
public class EduCourseApplyController {


    @Autowired
    EduCourseApplyService courseApplyService;

    @UserMultiSubmitLimit
    @PostMapping("/save")
    public BaseResult saveApply(@RequestBody EduCourseApply courseApply) {
        return courseApplyService.saveApply(courseApply);
    }

    @PostMapping("/already")
    public BaseResult queryCourseApplyByCourseIdAndUid(@RequestBody EduCourseApply courseApply) {
        return courseApplyService.queryCourseApplyByCourseIdAndUid(courseApply);
    }
}

