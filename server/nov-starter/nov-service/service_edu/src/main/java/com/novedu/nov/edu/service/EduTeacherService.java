package com.novedu.nov.edu.service;

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

    BaseResult<List<EduTeacher>> findAll();

    BaseResult removeTeacher(String id);
}
