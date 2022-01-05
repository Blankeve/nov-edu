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

    private String baseUrl = "http://42.193.169.224:8888/";

    public BaseResult uploadImg(MultipartFile img) {
        Map map = HttpUtils.doPostFile(baseUrl + "upload/img", img, "img");
        String path = "";
        if (map != null) {
            Map data = (Map) map.get("data");
            path = data.get("path").toString();
        }
        return BaseResult.successOrError(map == null).mapSet("path", baseUrl + path);
    }
}
