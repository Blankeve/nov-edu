import com.novedu.nov.StatisticsServiceApplication;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.statistics.client.EduClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author ：juam
 * @date ：2022/2/9 10:35
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = StatisticsServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class ClientTest {

    @Autowired
    EduClient eduClient;

    @Test
    public void test1(){
        BaseResult baseResult =eduClient.statisticsCoursePlayCount();
        System.out.println(baseResult.getCode());
    }
}
