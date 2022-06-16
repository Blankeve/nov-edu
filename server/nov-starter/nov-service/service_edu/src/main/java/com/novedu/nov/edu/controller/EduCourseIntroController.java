package com.novedu.nov.edu.controller;


import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.service.EduCourseIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程简介 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@RestController
@RequestMapping("/edu/course-intro")
public class EduCourseIntroController {

    @Autowired
    EduCourseIntroService courseIntroService;

    @GetMapping("/{id}")
    public BaseResult queryCourseIntroById(@PathVariable Integer id) {
        return courseIntroService.queryCourseIntroById(id);
    }
}

