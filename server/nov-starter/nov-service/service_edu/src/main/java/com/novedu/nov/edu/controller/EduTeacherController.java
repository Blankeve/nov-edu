package com.novedu.nov.edu.controller;


import com.novedu.nov.common.api.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/edu/edu-teacher")
public class EduTeacherController {

    @GetMapping("/test")
    public BaseResult test(){
        return BaseResult.success("操作成功");
    }
}

