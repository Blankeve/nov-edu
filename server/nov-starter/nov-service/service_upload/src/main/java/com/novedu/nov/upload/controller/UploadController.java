package com.novedu.nov.upload.controller;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.upload.helper.OssHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

@Api("文件上传的接口文档")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    OssHelper ossHelper;

    @PostMapping("/img")
    public BaseResult<Map> uploadImg(MultipartFile img) throws Exception {
        ossHelper.setFileRoot("avatar/");
        return BaseResult.success().mapSet("path",ossHelper.uploadFile(img));
    }

}
