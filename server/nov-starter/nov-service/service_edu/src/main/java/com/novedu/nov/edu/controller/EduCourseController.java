package com.novedu.nov.edu.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.model.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.EduCourseService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@Api("课程管理的接口文档")
@RestController
@RequestMapping("/edu/course")
public class EduCourseController {

    @Autowired
    EduCourseService eduCourseService;

    @PostMapping("/save")
    public BaseResult saveCourse(@RequestBody EduCourseInfoVO courseInfoVO){
        return eduCourseService.saveCourse(courseInfoVO);
    }

    @PostMapping("/list")
    public BaseResult findCourseDetail(EduCourseInfoVO courseInfoVO){
        return eduCourseService.findCourseDetail(courseInfoVO);
    }
}

