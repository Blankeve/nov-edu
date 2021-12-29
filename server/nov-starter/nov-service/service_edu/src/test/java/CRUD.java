import com.novedu.nov.EduServiceApplication;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.EduSubject;
import com.novedu.nov.edu.mapper.EduCourseMapper;
import com.novedu.nov.edu.service.EduSubjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
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

    @Autowired
    private EduSubjectService eduSubjectService;

    @Test
    public void test1() {
        List<EduCourse> eduCourses = eduCourseMapper.queryAll();
        System.out.println(eduCourses.size());
    }

    @Test
    public void test2() {
        List<EduCourse> eduCourses = eduCourseMapper.queryCourseTree();
        System.out.println(eduCourses.size());
    }

    @Test
    public void getParentSubjects() {
        Integer id = 1006;
        List<EduSubject> eduSubjects = eduSubjectService.list();
        List<Integer> nodes = new ArrayList<>();
        nodes.add(id);
        for (EduSubject o : eduSubjects) {
            if (o.getId().equals(id)) {
                addParentSubject(nodes, eduSubjects, o.getParentId());
                break;
            }
        }
    }

    private void addParentSubject(List list, List<EduSubject> eduSubjects, Integer pid) {
        if (pid == null || pid == 0)
            return;
        list.add(pid);
        for (EduSubject eduSubject : eduSubjects) {
            if (eduSubject.getId().equals(pid)) {
                addParentSubject(list, eduSubjects, eduSubject.getParentId());
            }
        }
    }
}
