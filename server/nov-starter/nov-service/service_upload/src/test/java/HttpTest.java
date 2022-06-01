import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.upload.UploadServiceApplication;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author ：juam
 * @date ：2022/1/5 15:41
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = UploadServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class HttpTest {

    @Test
    public void test1(){
        String url = "http://106.13.225.159:8888/upload/img";
        //传入参数可以为file或者filePath，在此处做转换
        File file = new File("C:\\Users\\dzyx\\Documents\\nov_log\\2022\\01\\05\\1.png");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse;
        HttpPost httppost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //设置浏览器兼容模式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //设置请求的编码格式
        builder.setCharset(Consts.UTF_8);
        builder.setContentType(ContentType.MULTIPART_FORM_DATA);
        //添加文件
        builder.addBinaryBody("img", file);
        HttpEntity reqEntity = builder.build();
        httppost.setEntity(reqEntity);

        try {
            httpResponse = httpClient.execute(httppost);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity httpEntity = httpResponse.getEntity();
                byte[] json= EntityUtils.toByteArray(httpEntity);
               String respJson = new String(json, "UTF-8");
                ObjectMapper objectMapper = new ObjectMapper();
               Map result = objectMapper.readValue(respJson, Map.class);
               Map data = (Map) result.get("data");
               String path = data.get("path").toString();
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){

    }
}
