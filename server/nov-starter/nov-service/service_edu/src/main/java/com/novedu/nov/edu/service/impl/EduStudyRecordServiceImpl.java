package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.edu.entity.EduStudyRecord;
import com.novedu.nov.edu.entity.dto.EduStudyRecordDTO;
import com.novedu.nov.edu.entity.vo.EduStudyRecordVO;
import com.novedu.nov.edu.mapper.EduStudyRecordMapper;
import com.novedu.nov.edu.service.EduStudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author juam
 * @since 2022-06-06
 */
@Service
public class EduStudyRecordServiceImpl extends ServiceImpl<EduStudyRecordMapper, EduStudyRecord> implements EduStudyRecordService {

    @Autowired
    EduStudyRecordMapper studyRecordMapper;

    @Override
    public IPage<EduStudyRecordVO> queryStudyRecordPage(Page page, EduStudyRecordDTO studyRecordDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(studyRecordDTO.getNickname()))
            queryWrapper.like("u.nickname", studyRecordDTO.getNickname());
        if (!StringUtils.isEmpty(studyRecordDTO.getCourseTitle()))
            queryWrapper.like("c.title", studyRecordDTO.getCourseTitle());
        if (!StringUtils.isEmpty(studyRecordDTO.getVideoTitle()))
            queryWrapper.like("v.title", studyRecordDTO.getVideoTitle());
        Date start = studyRecordDTO.getStartTime();
        Date end = studyRecordDTO.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("r.create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and r.create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        return studyRecordMapper.queryPage(page, queryWrapper);
    }
}
