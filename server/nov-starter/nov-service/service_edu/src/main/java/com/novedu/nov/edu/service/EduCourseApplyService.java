package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduCourseApply;

/**
 * <p>
 * 课程报名 服务类
 * </p>
 *
 * @author juam
 * @since 2022-02-11
 */
public interface EduCourseApplyService extends IService<EduCourseApply> {

    BaseResult saveApply(EduCourseApply courseApply);

    BaseResult queryCourseApplyByCourseIdAndUid(EduCourseApply courseApply);
}
