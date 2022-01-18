package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.CrmBanner;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.dto.EduCourseInfoDTO;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save")
    public BaseResult saveCourse(@RequestBody EduCourseInfoDTO courseInfoDTO) {
        return eduCourseService.saveCourse(courseInfoDTO);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeCourse(@PathVariable Integer id) {
        return eduCourseService.removeCourse(id);
    }

    @PostMapping("/detail/{id}")
    public BaseResult queryCourseDetail(@PathVariable Integer id) {
        return eduCourseService.queryCourseDetail(id);
    }

    @PostMapping("/id")
    public BaseResult queryCourseList(EduCourse id) {
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

    @GetMapping("/client-list")
    public BaseResult<List<EduCourse>> getClientCourseList(){
        return eduCourseService.getClientCourseList();
    }


}

