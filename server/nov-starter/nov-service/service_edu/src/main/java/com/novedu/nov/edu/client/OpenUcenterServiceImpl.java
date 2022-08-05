package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenUcenterServiceImpl implements OpenUcenterService {

    @Override
    public BaseResult queryUserRole(Long uid) {
        throw new ServiceInvokeFailureException(MsgConstants.UCENTER_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult getDashBoardInfo(String token) {
        throw new ServiceInvokeFailureException(MsgConstants.UCENTER_SERVICE_UNAVAIlABLE);

    }

    @Override
    public BaseResult getRecentAddUsers() {
        throw new ServiceInvokeFailureException(MsgConstants.UCENTER_SERVICE_UNAVAIlABLE);
    }

    @Override
    public BaseResult syncUsersCache() {
        throw new ServiceInvokeFailureException(MsgConstants.UCENTER_SERVICE_UNAVAIlABLE);
    }

}
