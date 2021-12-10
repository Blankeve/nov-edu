package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduTeacher;
import com.novedu.nov.edu.mapper.EduTeacherMapper;
import com.novedu.nov.edu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
    public BaseResult<List<EduTeacher>> getList(Page page, EduTeacher teacher) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(teacher.getName()))
            queryWrapper.like("name", teacher.getName());
        if (teacher.getLevel() != null)
            queryWrapper.eq("level", teacher.getLevel());
        if(teacher.getJoinDate()!=null)
            queryWrapper.apply("join_date > date_format({0},'%Y-%m-%d')",teacher.getJoinDate());
        return BaseResult.success(page(page, queryWrapper));
    }

    @Override
    public BaseResult removeTeacher(String id) {
        removeById(id);
        return BaseResult.success("删除成功");
    }


}
