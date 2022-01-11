import com.novedu.nov.EduServiceApplication;
import com.novedu.nov.edu.service.ProviderClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * @author ：juam
 * @date ：2022/1/11 14:04
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = EduServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class RestTemplateTest {

    @Autowired
    ProviderClient providerClient;

    @Test
    public void test1(){
       String str =  providerClient.service();
        System.out.println(str);
    }
}
