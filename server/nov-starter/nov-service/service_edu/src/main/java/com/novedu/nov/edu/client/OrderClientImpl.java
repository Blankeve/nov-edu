package com.novedu.nov.edu.client;

import com.novedu.nov.common.api.BaseResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderClientImpl implements OrderClient {
    @Override
    public BaseResult queryOrderByUidAndCourseId(Long id) {
        log.error("queryOrderByUidAndCourseId failed");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult queryOrderCount(Long teacherId) {
        log.error("queryOrderCount failed");
        return BaseResult.serviceInvokeFailure();
    }
}
