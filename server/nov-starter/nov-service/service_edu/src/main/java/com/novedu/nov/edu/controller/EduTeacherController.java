package com.novedu.nov.edu.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduTeacher;
import com.novedu.nov.edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    public BaseResult test(){
        return BaseResult.success("操作成功");
    }

    @GetMapping("/all")
    public BaseResult<List<EduTeacher>> findAll(){
        return eduTeacherService.findAll();
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public BaseResult removeTeacher(@PathVariable String id){
        return eduTeacherService.removeTeacher(id);
    }
}

