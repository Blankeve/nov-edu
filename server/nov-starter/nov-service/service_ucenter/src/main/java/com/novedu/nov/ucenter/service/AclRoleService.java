package com.novedu.nov.ucenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.ucenter.entity.dto.AssignUserRoleForm;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
public interface AclRoleService extends IService<AclRole> {

    BaseResult queryRoleList();

    BaseResult queryRolePage(Page page);

    BaseResult saveOrUpdateRole(AclRole role);

    BaseResult removeRole(Long id);

    BaseResult assignRoleByUid(AssignUserRoleForm params);

    BaseResult queryUserRole(Long uid);
}
