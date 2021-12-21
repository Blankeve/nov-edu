package com.novedu.nov.edu.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.model.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.EduCourseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-21
 */
@Api("课程的接口文档")
@RestController
@RequestMapping("/edu/course")
public class EduCourseController {

    @Autowired
    EduCourseService eduCourseService;

    @PostMapping("/add")
    public BaseResult addCourse(@RequestBody EduCourseInfoVO courseInfoVO){
        return eduCourseService.addCourse(courseInfoVO);
    }
}

