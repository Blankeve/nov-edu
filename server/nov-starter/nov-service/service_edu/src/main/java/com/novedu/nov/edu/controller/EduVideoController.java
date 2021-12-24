package com.novedu.nov.edu.controller;


import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduVideo;
import com.novedu.nov.edu.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@RestController
@RequestMapping("/edu/video")
public class EduVideoController {

    @Autowired
    EduVideoService videoService;

    @PostMapping("/save")
    public BaseResult saveCourse(@RequestBody EduVideo video){
        return videoService.saveVideo(video);
    }
}

