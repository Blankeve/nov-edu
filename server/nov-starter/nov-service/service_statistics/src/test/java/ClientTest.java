import com.novedu.nov.StatisticsServiceApplication;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.statistics.client.OpenEduService;
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
    OpenEduService openEduService;

    @Test
    public void test1(){
        BaseResult baseResult = openEduService.statisticsCoursePlayCount();
        System.out.println(baseResult.getCode());
    }
}
