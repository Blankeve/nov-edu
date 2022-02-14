package com.novedu.nov.ucenter.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author juam
 * @since 2022-02-14
 */
public interface AclUserService extends IService<AclUser> {
    BaseResult login(AclUser user);

    BaseResult register(AclUser user);

    BaseResult getMemberInfo(Long id);
}
