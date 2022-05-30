package com.novedu.nov.ucenter.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.ucenter.entity.AclUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novedu.nov.ucenter.entity.dto.AclUserRoleDTO;
import com.novedu.nov.ucenter.entity.vo.AclUserRoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2022-02-14
 */
@Repository
public interface AclUserMapper extends BaseMapper<AclUser> {

    IPage<AclUserRoleVO> queryPage(Page page, @Param("ew") Wrapper<AclUserRoleDTO> queryWrapper);

    AclUserRoleVO getInfoById(Long id);
}
