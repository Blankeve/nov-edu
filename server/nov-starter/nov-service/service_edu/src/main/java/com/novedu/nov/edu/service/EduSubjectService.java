package com.novedu.nov.edu.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;


import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-17
 */
public interface EduSubjectService extends IService<EduSubject> {

    BaseResult<Map> getSubjects();

    BaseResult addSubjects(List<EduSubject> subjects);

    BaseResult removeSubjects(List<EduSubject> subjects);

    BaseResult exportSubjects(HttpServletResponse response);

    BaseResult updateSubjects(Map<String,List<EduSubject>> eduSubjects);
}
