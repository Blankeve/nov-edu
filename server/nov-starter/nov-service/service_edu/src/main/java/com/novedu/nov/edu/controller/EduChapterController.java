package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduChapter;
import com.novedu.nov.edu.entity.dto.EduChapterInfoDTO;
import com.novedu.nov.edu.entity.vo.EduCourseInfoVO;
import com.novedu.nov.edu.service.EduChapterService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeChapter(@PathVariable Long id){
        return chapterService.removeChapter(id);
    }

    @PostMapping("/page")
    public BaseResult queryChapterPage(Page page, EduChapterInfoDTO chapterInfoDTO) {
        return chapterService.queryChapterPage(page, chapterInfoDTO);
    }

    @PostMapping("/detail/{id}")
    public BaseResult queryChapterDetail(@PathVariable Long id) {
        return chapterService.queryChapterDetail(id);
    }

    @PutMapping("/update-id")
    public BaseResult updateChapterById(@RequestBody EduChapter id){
        return chapterService.updateChapterById(id);
    }

    @PostMapping("/list-course")
    public BaseResult queryCourseByTeacherId(Long id){
        return chapterService.queryChaptersByCourseId(id);
    }
}

