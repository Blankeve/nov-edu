package com.novedu.nov.ucenter.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;

import java.util.Map;

public class OpenAuthServiceImpl implements OpenAuthService {
    @Override
    public BaseResult postAccessToken(Map<String, String> parameters) {
        throw new ServiceInvokeFailureException(MsgConstants.AUTH_SERVICE_UNAVAIlABLE);
    }
}
