package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduSubject;

import javax.servlet.http.HttpServletRequest;
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

    BaseResult<List<Integer>> getParentSubjects(Integer id);

    BaseResult addSubjects(List<EduSubject> subjects);

    BaseResult removeSubjects(List<EduSubject> subjects);

    BaseResult exportSubjects(HttpServletResponse response);

    BaseResult updateSubjects(Map<String, List<EduSubject>> eduSubjects);

    BaseResult getDashBoardInfo(HttpServletRequest request);

    BaseResult saveOrUpdateSubject(EduSubject subject);

    BaseResult removeSubject(Integer id);
}
