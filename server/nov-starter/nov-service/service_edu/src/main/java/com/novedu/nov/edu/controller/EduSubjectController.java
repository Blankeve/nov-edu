package com.novedu.nov.edu.controller;


import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduSubject;
import com.novedu.nov.edu.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/save-update")
    public BaseResult saveOrUpdate(@Validated @RequestBody EduSubject subject) {
        return eduSubjectService.saveOrUpdateSubject(subject);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeSubject(@PathVariable Integer id) {
        return eduSubjectService.removeSubject(id);
    }

    @GetMapping("/list")
    public BaseResult<Map> getSubjects() {
        return eduSubjectService.getSubjects();
    }

    @GetMapping("/list-parent/{id}")
    public BaseResult<List<Integer>> getParentSubjects(@PathVariable Integer id) {
        return eduSubjectService.getParentSubjects(id);
    }

    /*该方法有bug，已舍弃*/
    @PutMapping("/update")
    public BaseResult updateSubjects(@RequestBody Map<String, List<EduSubject>> eduSubjects) {
        return eduSubjectService.updateSubjects(eduSubjects);
    }


    @GetMapping("/export")
    public BaseResult exportSubjects(HttpServletResponse response) {
        return eduSubjectService.exportSubjects(response);
    }

    @ApiOperation("获取仪表盘数据")
    @GetMapping("/dashboard-info")
    public BaseResult getDashBoardInfo(HttpServletRequest request ) {
        return eduSubjectService.getDashBoardInfo(request);
    }
}

