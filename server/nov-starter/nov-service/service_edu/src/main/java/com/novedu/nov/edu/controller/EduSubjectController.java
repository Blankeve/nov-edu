package com.novedu.nov.edu.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduSubject;
import com.novedu.nov.edu.service.EduSubjectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-17
 */
@Api("课程科目的接口文档")
@RestController
@RequestMapping("/edu/edu-subject")
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;

    @GetMapping("/list")
    public BaseResult<EduSubject> getSubjects(){
        return eduSubjectService.getSubjects();
    }

    @GetMapping("/export")
    public BaseResult exportSubjects(HttpServletResponse response){
        return eduSubjectService.exportSubjects(response);
    }
}

