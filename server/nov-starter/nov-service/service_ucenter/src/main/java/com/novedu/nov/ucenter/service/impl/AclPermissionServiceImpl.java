package com.novedu.nov.ucenter.service.impl;

import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.ucenter.entity.AclPermission;
import com.novedu.nov.ucenter.mapper.AclPermissionMapper;
import com.novedu.nov.ucenter.service.AclPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@Service
public class AclPermissionServiceImpl extends ServiceImpl<AclPermissionMapper, AclPermission> implements AclPermissionService {

    @Override
    public BaseResult saveOrUpdatePermission(AclPermission permission) {
        return BaseResult.successOrError(saveOrUpdate(permission));
    }

    @Override
    public BaseResult removePermission(Long id) {
        return BaseResult.successOrError(removeById(id));
    }

    @Override
    public BaseResult<List<AclPermission>> queryTree() {
        List permissions = list();
        return BaseResult.success(TreeUtils.toTree(permissions,AclPermission.class));
    }
}
