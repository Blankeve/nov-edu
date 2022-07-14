import com.novedu.nov.EduServiceApplication;
import com.novedu.nov.common.util.IpAddressUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

/**
 * @author ：juam
 * @date ：2021/12/10 15:14
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = EduServiceApplication.class)
public class NoName {

    @Test
    public  void test1() {
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(password);
    }

    @Test
    public  void test2() {
        String ip = "59.57.155.9";
        String location = IpAddressUtils.getRealAddressByIP(ip);
        System.out.println(location);
    }

}
