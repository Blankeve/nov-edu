package com.novedu.nov.edu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.edu.entity.EduStudyRecord;
import com.novedu.nov.edu.entity.dto.EduStudyRecordDTO;
import com.novedu.nov.edu.entity.vo.EduStudyRecordVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author juam
 * @since 2022-06-06
 */
public interface EduStudyRecordMapper extends BaseMapper<EduStudyRecord> {

    IPage<EduStudyRecordVO> queryPage(Page page, @Param("ew") Wrapper<EduStudyRecordDTO> queryWrapper);

}
