package com.nov.controller;

import com.nov.common.model.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：juam
 * @date ：2021/12/8 10:34
 * @description：
 * @modified By：
 * @version:
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/a")
    public BaseResult test1(){
        return BaseResult.success("操作成功");
    }
}
