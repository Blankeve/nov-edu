package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.SysConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UserRoleClientImpl implements UserRoleClient {
    @Override
    public BaseResult queryUserRole(Long uid) {
        log.error("queryUserRole failed");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult getDashBoardInfo(String token) {
        log.error("getDashBoardInfo failed");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult getRecentAddUsers() {
        log.error("getRecentAddUsers failed");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult syncUsersCache() {
        log.error("getSyncUsersCache failed");
        return BaseResult.serviceInvokeFailure();
    }

    @Override
    public BaseResult<List<SysConfig>> getConfigListByKey(String key, Integer grade) {
        log.error("getConfigListByKey failed");
        return BaseResult.serviceInvokeFailure();
    }
}
