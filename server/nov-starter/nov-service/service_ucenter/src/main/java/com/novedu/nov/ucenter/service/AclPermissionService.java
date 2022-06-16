package com.novedu.nov.ucenter.service;

import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.ucenter.entity.AclPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.ucenter.entity.dto.AssignRolePermissionForm;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
public interface AclPermissionService extends IService<AclPermission> {

    BaseResult saveOrUpdatePermission(AclPermission permission);

    BaseResult removePermission(Long id);

    BaseResult queryTree();

    BaseResult queryPermissionByRoleId(Long id);

    BaseResult assignRolePermission(AssignRolePermissionForm params);
}
