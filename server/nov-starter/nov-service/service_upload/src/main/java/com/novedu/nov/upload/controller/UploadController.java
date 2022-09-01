package com.novedu.nov.upload.controller;


import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.upload.service.UploadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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

    @PostMapping("/img/ba64")
    public BaseResult<Map> uploadImgByBase64(@RequestBody Map img) {
        String image = (String) img.get("img");
        return uploadService.uploadImgByBase64(image);
    }

    @PostMapping("/video")
    public BaseResult<Map> uploadVideo(MultipartFile video) {
        return uploadService.uploadVideo(video);
    }

}
