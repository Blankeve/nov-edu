package com.novedu.nov.ucenter.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.ucenter.entity.AclPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2022-02-22
 */
@Repository
public interface AclPermissionMapper extends BaseMapper<AclPermission> {

    List<AclPermission> queryPermissionByRoleId(Long id);

}
