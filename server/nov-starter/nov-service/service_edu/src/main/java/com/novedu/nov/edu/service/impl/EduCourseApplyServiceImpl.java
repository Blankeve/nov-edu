package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduCourseApply;
import com.novedu.nov.edu.mapper.EduCourseApplyMapper;
import com.novedu.nov.edu.service.EduCourseApplyService;
import com.novedu.nov.edu.service.EduCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class EduCourseApplyServiceImpl extends ServiceImpl<EduCourseApplyMapper, EduCourseApply> implements EduCourseApplyService {

    @Autowired
    EduCourseService courseService;

    @Override
    public BaseResult saveApply(EduCourseApply courseApply) {
        BaseResult baseResult =queryCourseApplyByCourseIdAndUid(courseApply);
        if( BaseResult.success().getCode().equals(baseResult.getCode()))
            return BaseResult.error("已经报名过该课程");
        if(save(courseApply)){
            BaseResult baseResult2 = courseService.statisticsCourseApplyCount();
            if (BaseResult.success().getCode().equals(baseResult2.getCode()))
                log.info("同步课程学习人数成功");
            else
                log.info("同步课程学习人数失败");
            return BaseResult.success();
        }
        return BaseResult.error();
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
