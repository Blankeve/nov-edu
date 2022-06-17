package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.dto.EduCourseInfoDTO;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @UserMultiSubmitLimit
    @PostMapping("/save")
    public BaseResult saveCourse(@Validated @RequestBody EduCourseInfoDTO courseInfoDTO) {
        return eduCourseService.saveCourse(courseInfoDTO);
    }

    @PostMapping("/release")
    public BaseResult releaseCourse(@RequestBody EduCourseInfoDTO courseInfoDTO) {
        return eduCourseService.releaseCourse(courseInfoDTO);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeCourse(@PathVariable Long id) {
        return eduCourseService.removeCourse(id);
    }

    @PostMapping("/detail/{id}")
    public BaseResult queryCourseDetail(@PathVariable Long id) {
        return eduCourseService.queryCourseDetail(id);
    }

    @PostMapping("/id")
    public BaseResult queryCourseById(EduCourse id) {
        return eduCourseService.queryCourseById(id);
    }

    @GetMapping("/list")
    public BaseResult queryCourseList(EduCourseInfoVO courseInfoVO) {
        return eduCourseService.queryCourseList(courseInfoVO);
    }

    @PostMapping("/list-teacher")
    public BaseResult queryCourseByTeacherId(Long id) {
        return eduCourseService.queryCoursesByTeacherId(id);
    }

    @PostMapping("/export")
    public void exportCoursePage(HttpServletResponse response, Page page, EduCourseInfoDTO courseInfoDTO) {
         eduCourseService.exportCoursePage(response,page, courseInfoDTO);
    }

    @GetMapping("/export-all")
    public void exportAll(HttpServletResponse response) {
         eduCourseService.exportAll(response);
    }

    @PostMapping("/page")
    public BaseResult queryCoursePage(Page page, EduCourseInfoDTO courseInfoDTO) {
        return eduCourseService.queryCoursePage(page, courseInfoDTO);
    }

    @PostMapping("/page-client")
    public BaseResult queryClientCoursePage(Page page, EduCourseInfoDTO courseInfoDTO) {
        return eduCourseService.queryClientCoursePage(page, courseInfoDTO);
    }

    @PostMapping("/tree")
    public BaseResult queryCourseTree(Page page, EduCourseInfoDTO courseInfoDTO) {
        return eduCourseService.queryCourseTree(page, courseInfoDTO);
    }

    @PostMapping("/client-tree")
    public BaseResult queryClientCourseTree(EduCourseInfoDTO courseInfoDTO) {
        return eduCourseService.queryClientCourseTree(courseInfoDTO);
    }


    @ApiOperation("前台首页课程列表")
    @GetMapping("/client-list")
    public BaseResult<List<EduCourse>> getClientCourseList(){
        return eduCourseService.getClientCourseList();
    }

    @ApiOperation("前台最多人学习课程列表")
    @GetMapping("/client-apply")
    public BaseResult<List<EduCourse>> getClientApplyCourseList(){
        return eduCourseService.getClientApplyCourseList();
    }

    @ApiOperation("前台最多人购买课程列表")
    @GetMapping("/client-bought")
    public BaseResult<List<EduCourse>> getClientBoughtCourseList(){
        return eduCourseService.getClientBoughtCourseList();
    }

    @ApiOperation("统计课程播放量")
    @GetMapping("/statistics/course/playCount")
    public BaseResult statisticsCoursePlayCount(){
        return eduCourseService.statisticsCoursePlayCount();
    }

    @ApiOperation("统计课程报名人数")
    @GetMapping("/statistics/course/applyCount")
    public BaseResult statisticsCourseApplyCount(){
        return eduCourseService.statisticsCourseApplyCount();
    }

    @ApiOperation("统计课程购买量")
    @GetMapping("/statistics/course/buyCount")
    public BaseResult statisticsCourseBuyCount(){
        return eduCourseService.statisticsCourseBuyCount();
    }
}

