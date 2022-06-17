package com.novedu.nov.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.annotation.UserMultiSubmitLimit;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduVideo;
import com.novedu.nov.edu.entity.dto.EduStudyRecordDTO;
import com.novedu.nov.edu.entity.dto.EduVideoInfoDTO;
import com.novedu.nov.edu.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@Api("视频管理的接口文档")
@RestController
@RequestMapping("/edu/video")
public class EduVideoController {

    @Autowired
    EduVideoService videoService;

    @UserMultiSubmitLimit
    @PostMapping("/save")
    public BaseResult saveCourse(@Validated @RequestBody EduVideo video) {
        return videoService.saveVideo(video);
    }

    @PostMapping("/export")
    public void exportCoursePage(HttpServletResponse response, Page page, EduVideoInfoDTO videoInfoDTO) {
        videoService.exportVideoPage(response,page, videoInfoDTO);
    }

    @GetMapping("/export-all")
    public void exportAll(HttpServletResponse response) {
        videoService.exportAll(response);
    }

    @PostMapping("/page")
    public BaseResult queryVideoPage(Page page, EduVideoInfoDTO videoInfoDTO) {
        return videoService.queryVideoPage(page, videoInfoDTO);
    }

    @PostMapping("/history-watch/page")
    public BaseResult queryHistoryWatchPage(Page page) {
        return videoService.queryHistoryWatchPage(page);
    }

    @PostMapping("/study/record/page")
    public BaseResult queryStudyRecordPage(Page page, EduStudyRecordDTO studyRecordDTO) {
        return videoService.queryStudyRecordPage(page,studyRecordDTO);
    }

    @PostMapping("/detail/{id}")
    public BaseResult queryVideoDetail(@PathVariable Long id) {
        return videoService.queryVideoDetail(id);
    }

    @PostMapping("/detail-client/{id}")
    public BaseResult queryClientVideo(@PathVariable Long id, HttpServletRequest request) {
        return videoService.queryClientVideo(id,request);
    }

    @ApiOperation("删除")
    @DeleteMapping("/remove/{id}")
    public BaseResult removeVideo(@PathVariable Long id) {
        return videoService.removeVideo(id);
    }
}

