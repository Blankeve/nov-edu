package com.novedu.nov.edu.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduCourseApply;
import com.novedu.nov.edu.mapper.EduCourseApplyMapper;
import com.novedu.nov.edu.service.EduCourseApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程报名 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-11
 */
@Service
public class EduCourseApplyServiceImpl extends ServiceImpl<EduCourseApplyMapper, EduCourseApply> implements EduCourseApplyService {

    @Override
    public BaseResult saveApply(EduCourseApply courseApply) {
        return BaseResult.successOrError(save(courseApply));
    }

    @Override
    public BaseResult queryCourseApplyByCourseIdAndUid(EduCourseApply courseApply) {
        courseApply = query().eq("course_id",courseApply.getCourseId()).eq("uid",courseApply.getUid()).one();
        if(courseApply != null)
            return BaseResult.success();
        else
            return BaseResult.error();
    }


}
