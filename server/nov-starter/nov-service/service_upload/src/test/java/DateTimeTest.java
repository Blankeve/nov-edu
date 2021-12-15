import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：juam
 * @date ：2021/12/15 10:22
 * @description：
 * @modified By：
 * @version:
 */
public class DateTimeTest {

    public static void main(String[] args) {
        String dateTime = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        System.out.println(dateTime);
    }
}
