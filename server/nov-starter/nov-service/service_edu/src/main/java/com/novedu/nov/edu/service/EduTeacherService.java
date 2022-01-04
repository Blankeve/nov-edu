package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author juam
 * @since 2021-12-08
 */
public interface EduTeacherService extends IService<EduTeacher> {

    BaseResult<List<EduTeacher>> findTeacherList(Page page, EduTeacher teacher);

    BaseResult removeTeacher(String id);

    BaseResult saveTeacher(EduTeacher teacher);

    BaseResult editTeacher(EduTeacher teacher);

    BaseResult<EduTeacher> findTeacherOne(String id);

    BaseResult<List<EduTeacher>> findAll();
}
