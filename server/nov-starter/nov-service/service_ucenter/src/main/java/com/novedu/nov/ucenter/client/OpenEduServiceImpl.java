package com.novedu.nov.ucenter.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;

public class OpenEduServiceImpl implements OpenEduService {
    @Override
    public BaseResult clearBind(String uid) {
        throw new ServiceInvokeFailureException(MsgConstants.EDU_SERVICE_UNAVAIlABLE);
    }
}
