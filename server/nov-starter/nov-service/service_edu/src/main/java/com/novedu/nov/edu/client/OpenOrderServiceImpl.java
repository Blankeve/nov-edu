package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenOrderServiceImpl implements OpenOrderService {

    @Override
    public BaseResult queryOrderByUidAndCourseId(Long id, Long uid) {
        throw new ServiceInvokeFailureException(MsgConstants.ORDER_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult queryOrderCount(Long teacherId) {
        throw new ServiceInvokeFailureException(MsgConstants.ORDER_SERVICE_UNAVAIlABLE);
    }
}
