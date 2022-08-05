package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenStatisticsServiceImpl implements OpenStatisticsService {


    @Override
    public BaseResult statisticsAWeekUserRegisterAndAccessCount() {
        throw new ServiceInvokeFailureException(MsgConstants.STATISTICS_SERVICE_UNAVAIlABLE);
    }
}
