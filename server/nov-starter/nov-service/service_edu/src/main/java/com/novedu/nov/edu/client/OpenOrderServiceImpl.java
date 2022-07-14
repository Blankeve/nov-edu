package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenOrderServiceImpl implements OpenOrderService {
    @Override
    public BaseResult queryOrderByUidAndCourseId(Long id, Long uid) {
        log.error("queryOrderByUidAndCourseId failed");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult queryOrderCount(Long teacherId) {
        log.error("queryOrderCount failed");
        return BaseResult.serviceInvokeFailure();
    }
}
