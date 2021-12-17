package com.novedu.nov.edu.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.edu.entity.EduSubject;
import com.novedu.nov.edu.mapper.EduSubjectMapper;
import com.novedu.nov.edu.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author juam
 * @since 2021-12-17
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public BaseResult<EduSubject> getSubjects() {
        List<EduSubject> eduSubjects = list();
        return BaseResult.success(TreeUtils.build(eduSubjects));
    }

    @Override
    public BaseResult exportSubjects(HttpServletResponse response) {
//        List <EduSubject>  eduSubjectVoList= new ArrayList<>();
//        eduSubjectVoList.add(new EduSubject());
//        try {
//            ExcelUtils.defaultExport("所有科目","所有科目",EduSubject.class,eduSubjectVoList,response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return BaseResult.success("导出成功");
    }


}
