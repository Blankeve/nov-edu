package com.novedu.nov.edu.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduCourseIntro;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
public interface EduCourseIntroService extends IService<EduCourseIntro> {

    BaseResult queryCourseIntroById(Integer id);
}
