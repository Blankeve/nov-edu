import com.novedu.nov.EduServiceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author ：juam
 * @date ：2022/1/14 15:17
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = EduServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class RedisTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test1(){
        redisTemplate.boundValueOps("test").set("hello.redis");
       String msg=  redisTemplate.opsForValue().get("test").toString();
        System.out.println(msg);
    }
}
