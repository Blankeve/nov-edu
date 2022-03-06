package com.novedu.nov.order.client;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.order.entity.EduCourseApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EduClientImpl implements EduClient {


    @Override
    public BaseResult queryCourseDetail(Long id) {
        log.error("查询课程信息失败");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult saveApply(EduCourseApply courseApply) {
        log.error("saveApply fail");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult queryCourseApplyByCourseIdAndUid(EduCourseApply courseApply) {
        log.error("同步课程购买数失败");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult statisticsCourseApplyCount() {
        log.error("同步课程购买数失败");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult statisticsCourseBuyCount() {
        log.error("同步课程购买数失败");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult queryTeacherIdByUid(String uid) {
        log.error("通过uid查询讲师id失败");
        return BaseResult.serviceInvokeFailure();
    }
}
