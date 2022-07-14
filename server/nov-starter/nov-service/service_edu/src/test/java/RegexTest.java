import com.novedu.nov.EduServiceApplication;
import com.novedu.nov.edu.entity.CrmBanner;
import com.novedu.nov.edu.service.CrmBannerService;
import com.novedu.nov.edu.service.EduCourseService;
import com.novedu.nov.edu.service.EduTeacherService;
import com.novedu.nov.edu.service.EduVideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author ：juam
 * @date ：2022/2/15 14:39
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest(classes = EduServiceApplication.class)
@ExtendWith(SpringExtension.class)
public class RegexTest {

    @Autowired
    EduTeacherService teacherService;

    @Autowired
    EduCourseService courseService;

    @Autowired
    EduVideoService videoService;



    @Autowired
    CrmBannerService bannerService;

    private String url = "http://106.13.225.159:8888/img/2022/01/24/1596929522266148864.jpg";

    @Test
    public void test1(){
//       List<EduTeacher> teachers= teacherService.list();
//       teachers.forEach(t->t.setAvatar(t.getAvatar().replaceFirst("http://.*?:","http://159.75.234.20:")));
//       teacherService.updateBatchById(teachers);
//        List<EduCourse> teachers= courseService.list();
//        teachers.forEach(t->t.setCover(t.getCover().replaceFirst("http://.*?:","http://159.75.234.20:")));
//        courseService.updateBatchById(teachers);
        List<CrmBanner> videos= bannerService.list();
        videos.forEach(t->{
            if(StringUtils.hasText(t.getImageUrl())){
                t.setImageUrl(t.getImageUrl().replaceFirst("http://.*?:","http://159.75.234.20:"));
            }
        });
        bannerService.updateBatchById(videos);
    }
}
