import com.novedu.nov.EduServiceApplication;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.mapper.EduCourseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * @author ：juam
 * @date ：2021/12/24 10:38
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = EduServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class CRUD {

    @Autowired
    private EduCourseMapper eduCourseMapper;

    @Test
    public void test1(){
        List<EduCourse> eduCourses = eduCourseMapper.queryAll();
        System.out.println(eduCourses.size());
    }

    @Test
    public void test2(){
        List<EduCourse> eduCourses = eduCourseMapper.queryCoursesForTreeData();
        System.out.println(eduCourses.size());
    }
}
