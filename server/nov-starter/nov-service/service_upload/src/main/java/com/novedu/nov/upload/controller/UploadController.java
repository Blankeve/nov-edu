package com.novedu.nov.upload.controller;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.helper.SnowFlake;
import com.novedu.nov.upload.service.UploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：juam
 * @date ：2021/12/14 14:28
 * @description：
 * @modified By：
 * @version:
 */

@Api("上传的接口文档")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    UploadService uploadService;


    @PostMapping("/img")
    public BaseResult<Map> uploadImg(MultipartFile img) {
        return uploadService.uploadImg(img);
    }

    @PostMapping("/video")
    public BaseResult<Map> uploadVideo(MultipartFile video) {
        return uploadService.uploadVideo(video);
    }

}
