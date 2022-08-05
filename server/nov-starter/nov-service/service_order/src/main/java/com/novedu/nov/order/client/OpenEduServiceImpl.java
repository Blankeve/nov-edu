package com.novedu.nov.order.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;
import com.novedu.nov.order.entity.EduCourseApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OpenEduServiceImpl implements OpenEduService {

    @Override
    public BaseResult queryCourseDetail(Long id) {
        throw new ServiceInvokeFailureException(MsgConstants.EDU_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult saveApply(EduCourseApply courseApply) {
        throw new ServiceInvokeFailureException(MsgConstants.EDU_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult queryCourseApplyByCourseIdAndUid(EduCourseApply courseApply) {
        throw new ServiceInvokeFailureException(MsgConstants.EDU_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult statisticsCourseApplyCount() {
        throw new ServiceInvokeFailureException(MsgConstants.EDU_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult statisticsCourseBuyCount() {
        throw new ServiceInvokeFailureException(MsgConstants.EDU_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult queryTeacherIdByUid(String uid) {
        throw new ServiceInvokeFailureException(MsgConstants.EDU_SERVICE_UNAVAIlABLE);
    }
}
