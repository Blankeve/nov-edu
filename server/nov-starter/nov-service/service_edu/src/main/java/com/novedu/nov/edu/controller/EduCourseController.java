package com.novedu.nov.edu.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduTeacher;
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

    @PostMapping("/detail")
    public BaseResult findCourseDetail(EduCourseInfoVO courseInfoVO){
        return eduCourseService.findCourseDetail(courseInfoVO);
    }

    @PostMapping("/list")
    public BaseResult queryCourseList(EduCourseInfoVO courseInfoVO){
        return eduCourseService.queryCourseList(courseInfoVO);
    }
    @PostMapping("/list-teacher")
    public BaseResult queryCourseByTeacherId(Long id){
        return eduCourseService.queryCoursesByTeacherId(id);
    }
    @PostMapping("/tree")
    public BaseResult queryCoursesForTreeData(EduCourseInfoVO courseInfoVO){
        return eduCourseService.queryCoursesForTreeData(courseInfoVO);
    }
}

