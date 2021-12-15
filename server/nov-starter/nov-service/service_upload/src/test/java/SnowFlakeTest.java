import com.novedu.nov.common.helper.SnowFlake;
import com.novedu.nov.upload.UploadServiceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author ：juam
 * @date ：2021/12/15 9:33
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = UploadServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class SnowFlakeTest {

    @Autowired
    SnowFlake snowFlakeUtils;

    @Test
    public void test1() throws Exception {
        System.out.println(snowFlakeUtils.nextValue());
    }
}
