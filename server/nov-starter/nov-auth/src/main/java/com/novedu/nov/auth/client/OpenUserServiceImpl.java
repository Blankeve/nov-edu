package com.novedu.nov.auth.client;

import com.novedu.nov.common.constants.MsgConstants;
import com.novedu.nov.common.entity.UserDTO;
import com.novedu.nov.common.exception.ServiceInvokeFailureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenUserServiceImpl implements OpenUserService {
    @Override
    public UserDTO loadUserByUsername(String username) {
        throw new ServiceInvokeFailureException(MsgConstants.UCENTER_SERVICE_UNAVAIlABLE);
    }
}
