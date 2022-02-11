package com.novedu.nov.upload.helper;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.novedu.nov.common.helper.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author ：juam
 * @date ：2021/12/14 14:43
 * @description：
 * @modified By：
 * @version:
 */
@Component
public class OssHelper {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    //文件存储根目录
    private String fileRoot;

    private String filePath;

    @Autowired
    SnowFlake snowFlake;

    public void setFileRoot(String fileRoot) {
        this.fileRoot = fileRoot;
    }

    /**
     * 1、单个文件上传
     *
     * @param file
     * @return 返回完整URL地址
     */
    public String uploadFile(MultipartFile file) throws Exception {
        String fileUrl = uploadImg2Oss(file);
        return fileUrl.trim();
    }

    /**
     * 1、单个文件上传(指定文件名（带后缀）)
     *
     * @param file
     * @return 返回完整URL地址
     */
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, fileName);
            return fileName;
        } catch (Exception e) {
            return "上传失败";
        }
    }

    /**
     * 2、多文件上传
     *
     * @param fileList
     * @return 返回完整URL，逗号分隔
     */
    public String uploadFile(List<MultipartFile> fileList) throws Exception {
        String fileUrl = "";
        String str = "";
        String photoUrl = "";
        for (int i = 0; i < fileList.size(); i++) {
            fileUrl = uploadImg2Oss(fileList.get(i));
            str = getFileUrl(filePath);
            if (i == 0) {
                photoUrl = str;
            } else {
                photoUrl += "," + str;
            }
        }
        return photoUrl.trim();
    }


    //获取去掉参数的完整路径
    private String getShortUrl(String url) {
        String[] imgUrls = url.split("\\?");
        return imgUrls[0].trim();
    }

    // 获得url链接
    private String getFileUrl(String key) {
        // 设置URL过期时间为20年 3600l* 1000*24*365*20
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 20);
        // 生成URL
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return getShortUrl(url.toString());
        }
        return null;
    }

    // 上传文件
    private String uploadImg2Oss(MultipartFile file) throws Exception {
        //1、限制最大文件为20M
        if (file.getSize() > 1024 * 1024 * 20) {
            return "图片太大";
        }

        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase(); //文件后缀
        String dateTime = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        String name = dateTime + snowFlake.nextValue() + suffix;

        try {
            InputStream inputStream = file.getInputStream();
            uploadFile2OSS(inputStream, name);
            return getFileUrl(filePath);
        } catch (Exception e) {
            return "上传失败";
        }
    }


    // 上传文件（指定文件名）
    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            filePath = fileRoot + fileName;
            PutObjectResult putResult = ossClient.putObject(bucketName, filePath, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {

        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    private static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".mapping")) {
            return "text/mapping";
        }
        //PDF
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        return "image/jpeg";
    }

}
