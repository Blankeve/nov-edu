package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduTeacher;
import com.novedu.nov.edu.entity.dto.EduTeacherDTO;
import com.novedu.nov.edu.entity.dto.UserBindTeacherForm;
import com.novedu.nov.edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-08
 */
@Api("讲师管理的接口文档")
@RestController
@RequestMapping("/edu/edu-teacher")
public class EduTeacherController {

    @Autowired
    EduTeacherService eduTeacherService;

    @GetMapping("/test")
    public BaseResult test() {
        return BaseResult.success("操作成功");
    }

    @GetMapping("/list")
    public BaseResult<List<EduTeacher>> queryTeacherPage(Page page, EduTeacherDTO teacher) {
        return eduTeacherService.queryTeacherPage(page, teacher);
    }

    @PostMapping("/export")
    public void exportTeacherPage(HttpServletResponse response, Page page, EduTeacherDTO teacher) {
        eduTeacherService.exportTeacherPage(response,page, teacher);
    }

    @GetMapping("/export-all")
    public void exportAll(HttpServletResponse response) {
        eduTeacherService.exportAll(response);
    }

    @GetMapping("/all")
    public BaseResult<List<EduTeacher>> all() {
        return eduTeacherService.findAll();
    }

    @GetMapping("/all-bind/{id}")
    public BaseResult queryAllAndHadBind(@PathVariable String id) {
        return eduTeacherService.queryAllAndHadBind(id);
    }

    @GetMapping("/info/{id}")
    public BaseResult<EduTeacher> info(@PathVariable String id) {
        return eduTeacherService.findTeacherOne(id);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeTeacher(@PathVariable String id) {
        return eduTeacherService.removeTeacher(id);
    }

    @PostMapping("/bind/{uid}")
    public BaseResult queryTeacherIdByUid(@PathVariable String uid) {
        return BaseResult.success(eduTeacherService.query().eq("uid", uid).one().getId());
    }

    @PostMapping("/clear-bind/{uid}")
    public BaseResult clearBind(@PathVariable String uid) {
        return eduTeacherService.clearBind(uid);
    }

    @PostMapping("/save")
    public BaseResult saveTeacher(@RequestBody @Validated EduTeacher teacher) {
        return eduTeacherService.saveTeacher(teacher);
    }

    @PutMapping("/edit")
    public BaseResult editTeacher(@RequestBody @Validated EduTeacher teacher) {
        return eduTeacherService.editTeacher(teacher);
    }

    @PutMapping("/update-bind")
    public BaseResult updateBindTeacher(@RequestBody UserBindTeacherForm bindTeacherForm) {
        return eduTeacherService.updateBindTeacher(bindTeacherForm);
    }

    @GetMapping("/client-list")
    public BaseResult<List<EduTeacher>> getClientTeacherList(){
        return eduTeacherService.getClientTeacherList();
    }
}

