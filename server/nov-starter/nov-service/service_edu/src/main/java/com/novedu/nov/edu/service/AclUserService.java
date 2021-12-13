package com.novedu.nov.edu.service;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.AclUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-13
 */
public interface AclUserService extends IService<AclUser> {

    BaseResult login(AclUser user);

    BaseResult getInfo(String token);
}
