package com.novedu.nov.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclRole;
import com.novedu.nov.ucenter.entity.AclUserRole;
import com.novedu.nov.ucenter.entity.dto.AssignUserRoleForm;
import com.novedu.nov.ucenter.mapper.AclRoleMapper;
import com.novedu.nov.ucenter.service.AclRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.ucenter.service.AclUserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@Service
public class AclRoleServiceImpl extends ServiceImpl<AclRoleMapper, AclRole> implements AclRoleService {

    @Autowired
    AclUserRoleService userRoleService;

    @Override
    public BaseResult queryRoleList() {
        return BaseResult.success(list());
    }

    @Override
    public BaseResult queryRolePage(Page page) {
        return BaseResult.success(page(page, null));
    }

    @Override
    public BaseResult saveOrUpdateRole(AclRole role) {
        return BaseResult.successOrError(saveOrUpdate(role));
    }

    @Override
    public BaseResult removeRole(Long id) {
        return BaseResult.successOrError(removeById(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult assignRoleByUid(AssignUserRoleForm params) {
        Long uid = params.getUid();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        userRoleService.remove(queryWrapper);
        AclUserRole userRole = new AclUserRole();
        BeanUtils.copyProperties(params, userRole);
        return BaseResult.successOrError(userRoleService.save(userRole));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult queryUserRole(Long uid) {
        AclUserRole userRole = userRoleService.query().eq("uid", uid).one();
        AclRole role = query().eq("id", userRole.getRoleId()).one();
        return BaseResult.success(role);
    }
}
