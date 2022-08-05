package com.novedu.nov.statistics.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;

public class OpenUcenterServiceImpl implements OpenUcenterService {

    @Override
    public BaseResult syncRegisterLoginCount() {
        throw new ServiceInvokeFailureException(MsgConstants.UCENTER_SERVICE_UNAVAIlABLE);
    }
}
