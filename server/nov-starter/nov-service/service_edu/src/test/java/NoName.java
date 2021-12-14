import com.novedu.nov.EduServiceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.DigestUtils;

/**
 * @author ：juam
 * @date ：2021/12/10 15:14
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = EduServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class NoName {

    @Test
    public  void test1() {
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(password);
    }
}
