package com.novedu.nov.edu.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;


import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-17
 */
public interface EduSubjectService extends IService<EduSubject> {

    BaseResult<EduSubject> getSubjects();

    BaseResult exportSubjects(HttpServletResponse response);
}
