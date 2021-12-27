package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@RestController
@RequestMapping("/edu/chapter")
public class EduChapterController {


    @Autowired
    EduChapterService chapterService;

    @PostMapping("/save")
    public BaseResult saveCourse(@RequestBody EduChapter chapter){
        return chapterService.saveChapter(chapter);
    }

    @PostMapping("/page")
    public BaseResult queryChapterPage(Page page, EduCourseInfoVO courseInfoVO) {
        return chapterService.queryChapterPage(page, courseInfoVO);
    }

    @PostMapping("/list-course")
    public BaseResult queryCourseByTeacherId(Long id){
        return chapterService.queryChaptersByCourseId(id);
    }
}

