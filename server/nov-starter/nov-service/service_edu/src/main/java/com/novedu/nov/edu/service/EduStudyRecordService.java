package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduStudyRecord;
import com.novedu.nov.edu.entity.dto.EduStudyRecordDTO;
import com.novedu.nov.edu.entity.vo.EduStudyRecordVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author juam
 * @since 2022-06-06
 */
public interface EduStudyRecordService extends IService<EduStudyRecord> {

    IPage<EduStudyRecordVO> queryStudyRecordPage(Page page, EduStudyRecordDTO studyRecordDTO);
}
