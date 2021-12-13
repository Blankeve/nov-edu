package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduTeacher;
import com.novedu.nov.edu.mapper.EduTeacherMapper;
import com.novedu.nov.edu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author juam
 * @since 2021-12-08
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {


    @Override
    public BaseResult<List<EduTeacher>> findTeacherList(Page page, EduTeacher teacher) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(teacher.getName()))
            queryWrapper.like("name", teacher.getName());
        if (teacher.getLevel() != null)
            queryWrapper.eq("level", teacher.getLevel());
        if (teacher.getJoinDate() != null)
            queryWrapper.apply("create_time > date_format({0},'%Y-%m-%d')", teacher.getCreateTime());
        queryWrapper.orderByDesc("create_time");
        return BaseResult.success(page(page, queryWrapper));
    }

    @Override
    public BaseResult removeTeacher(String id) {
        return BaseResult.successOrError(removeById(id));
    }

    @Override
    public BaseResult saveTeacher(EduTeacher teacher) {
        return BaseResult.successOrError(save(teacher));
    }

    @Override
    public BaseResult editTeacher(EduTeacher teacher) {
        return BaseResult.successOrError(updateById(teacher));
    }

    @Override
    public BaseResult<EduTeacher> findTeacherOne(String id) {
        return BaseResult.success(getById(id));
    }


}
