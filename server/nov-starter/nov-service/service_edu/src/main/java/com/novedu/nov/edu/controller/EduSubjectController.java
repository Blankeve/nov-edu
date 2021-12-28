package com.novedu.nov.edu.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduSubject;
import com.novedu.nov.edu.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/edu/subject")
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;

    @GetMapping("/list")
    public BaseResult<Map> getSubjects() {
        return eduSubjectService.getSubjects();
    }

    @GetMapping("/list-parent/{id}")
    public BaseResult<List<Integer>> getParentSubjects(@PathVariable Integer id) {
        return eduSubjectService.getParentSubjects(id);
    }

    @PutMapping("/update")
    public BaseResult updateSubjects(@RequestBody Map<String, List<EduSubject>> eduSubjects) {
        return eduSubjectService.updateSubjects(eduSubjects);
    }


    @GetMapping("/export")
    public BaseResult exportSubjects(HttpServletResponse response) {
        return eduSubjectService.exportSubjects(response);
    }
}

