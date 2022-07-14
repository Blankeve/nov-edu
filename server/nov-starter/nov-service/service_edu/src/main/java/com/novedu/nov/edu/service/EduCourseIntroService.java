package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduCourseIntro;

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
