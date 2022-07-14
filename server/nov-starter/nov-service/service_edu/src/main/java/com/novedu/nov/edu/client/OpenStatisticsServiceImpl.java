package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenStatisticsServiceImpl implements OpenStatisticsService {
    @Override
    public BaseResult statisticsAWeekUserRegisterAndAccessCount() {
        log.error("statisticsAWeekUserRegisterAndAccessCount failed");
        return BaseResult.serviceInvokeFailure();
    }
}
