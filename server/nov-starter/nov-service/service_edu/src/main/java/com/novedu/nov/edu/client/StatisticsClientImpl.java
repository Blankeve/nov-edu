package com.novedu.nov.edu.client;

import com.novedu.nov.common.api.BaseResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatisticsClientImpl implements StatisticsClient {
    @Override
    public BaseResult statisticsAWeekUserRegisterAndAccessCount() {
        log.error("statisticsAWeekUserRegisterAndAccessCount failed");
        return BaseResult.serviceInvokeFailure();
    }
}
