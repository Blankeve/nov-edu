package com.novedu.nov.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.ucenter.entity.AclRole;
import com.novedu.nov.ucenter.mapper.AclRoleMapper;
import com.novedu.nov.ucenter.service.AclRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@Service
public class AclRoleServiceImpl extends ServiceImpl<AclRoleMapper, AclRole> implements AclRoleService {

    @Override
    public BaseResult queryRoleList() {
        return BaseResult.success(query().eq("is_deleted",0).list());
    }

    @Override
    public BaseResult queryRolePage(Page page, AclRole role) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_deleted",0);
        return BaseResult.success(page(page,queryWrapper));
    }

    @Override
    public BaseResult saveOrUpdateRole(AclRole role) {
        return BaseResult.successOrError(saveOrUpdate(role));
    }

    @Override
    public BaseResult removeRole(Long id) {
        return BaseResult.successOrError(removeById(id));
    }
}
