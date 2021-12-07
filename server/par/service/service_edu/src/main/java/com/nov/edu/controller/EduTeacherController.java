package com.nov.edu.controller;


import com.nov.common.model.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author me
 * @since 2021-12-07
 */
@RestController
@RequestMapping("/edu/edu-teacher")
public class EduTeacherController {

    @GetMapping("/test")
    public BaseResult test(){
        return BaseResult.success("测试成功");
    }
}

