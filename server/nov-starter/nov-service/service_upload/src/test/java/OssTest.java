import com.novedu.nov.upload.UploadServiceApplication;
import com.novedu.nov.upload.helper.OssHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author ：juam
 * @date ：2021/12/14 14:59
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = UploadServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class OssTest {

    @Autowired
    OssHelper ossHelper;

    @Test
    public void test1() throws FileNotFoundException {
        File file = new File("C:\\Users\\dzyx\\Pictures\\Saved Pictures\\1.png");
        String fileName = file.getName();
       String name = ossHelper.uploadFile2OSS(new FileInputStream(file),fileName);
        System.out.println(fileName);
    }
}
