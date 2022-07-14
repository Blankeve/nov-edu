package com.novedu.nov.edu.client;

import com.novedu.nov.common.base.BaseResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenUcenterServiceImpl implements OpenUcenterService {
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

}
