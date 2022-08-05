package com.novedu.nov.order.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;

public class OpenUcenterServiceImpl implements OpenUcenterService {

    @Override
    public BaseResult getMemberInfo(Long id) {
        throw new ServiceInvokeFailureException(MsgConstants.UCENTER_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult queryUserRole(Long uid) {
        throw new ServiceInvokeFailureException(MsgConstants.UCENTER_SERVICE_UNAVAIlABLE);
    }
}
