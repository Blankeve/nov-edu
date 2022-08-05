package com.novedu.nov.statistics.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;

public class OpenEduServiceImpl implements OpenEduService {

    @Override
    public BaseResult statisticsCoursePlayCount() {
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
}
