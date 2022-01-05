package com.novedu.nov.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author ：juam
 * @date ：2022/1/5 16:22
 * @description：
 * @modified By：
 * @version:
 */
@Slf4j
public class HttpUtils {

    public static Map doPostFile(String url, MultipartFile file, String paramsName) {
        if (!StringUtils.hasText(paramsName))
            paramsName = "file";
        //传入参数可以为file或者filePath，在此处做转换
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        HttpPost httppost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //设置浏览器兼容模式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //设置请求的编码格式
        builder.setCharset(Consts.UTF_8);
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);
        File file1 = null;
        try {
            String originalFilename = file.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file1 = File.createTempFile(filename[0].length()<3?filename[0]+"xxx":filename[0], "."+filename[1]);
            file.transferTo(file1);
            file1.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //添加文件
        builder.addBinaryBody(paramsName, file1);
        HttpEntity reqEntity = builder.build();
        httppost.setEntity(reqEntity);

        try {
            httpResponse = httpClient.execute(httppost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                byte[] json = EntityUtils.toByteArray(httpEntity);
                String respJson = new String(json, "UTF-8");
                log.info("==========Remote return============");
                log.info(respJson);
                log.info("===================================");
                ObjectMapper objectMapper = new ObjectMapper();
                Map result = objectMapper.readValue(respJson, Map.class);
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
