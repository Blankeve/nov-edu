package com.novedu.nov.upload.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.HttpUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author ：juam
 * @date ：2022/1/5 15:41
 * @description：
 * @modified By：
 * @version:
 */
@Service
public class UploadService {

    private String baseUrl = "http://42.193.169.224:8888";
//    private String baseUrl = "http://localhost:9999";

    public BaseResult uploadImg(MultipartFile img) {
        Map result = HttpUtils.doPostFile(baseUrl + "/upload/img", img, "img");
        String path;
        if (result != null) {
            if ("200".equals(result.get("code").toString())) {
                Map data = (Map) result.get("data");
                path = data.get("path").toString();
                return BaseResult.success().mapSet("path", baseUrl + path);
            } else
                return BaseResult.error(result.get("msg").toString());
        } else
            return BaseResult.error();
    }

    public BaseResult<Map> uploadVideo(MultipartFile video) {
        Map result = HttpUtils.doPostFile(baseUrl + "/upload/video", video, "video");
        String path;
        if (result != null) {
            if ("200".equals(result.get("code").toString())) {
                Map data = (Map) result.get("data");
                path = data.get("path").toString();
                return BaseResult.success().mapSet("path", baseUrl + path)
                        .mapSet("size", data.get("size").toString())
                        .mapSet("videoOriginalName", data.get("name").toString())
                        .mapSet("duration", data.get("duration").toString())
                        ;
            } else
                return BaseResult.error(result.get("msg").toString());
        } else
            return BaseResult.error();
    }
}
