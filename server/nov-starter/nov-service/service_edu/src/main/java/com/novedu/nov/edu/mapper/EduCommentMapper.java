package com.novedu.nov.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.edu.entity.EduComment;
import com.novedu.nov.edu.entity.dto.EduUserCommentDTO;
import com.novedu.nov.edu.entity.vo.EduUserCommentVO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2022-01-26
 */
@Repository
public interface EduCommentMapper extends BaseMapper<EduComment> {

    IPage<EduUserCommentVO> queryPage(Page page, EduUserCommentDTO comment);

}
