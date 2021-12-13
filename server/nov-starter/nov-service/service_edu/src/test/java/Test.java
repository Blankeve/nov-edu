import org.springframework.util.DigestUtils;

/**
 * @author ：juam
 * @date ：2021/12/10 15:14
 * @description：
 * @modified By：
 * @version:
 */
public class Test {
    public static void main(String[] args) {
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(password);
    }
}
