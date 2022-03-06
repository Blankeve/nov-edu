package com.novedu.nov.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.edu.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.novedu.nov.edu.entity.dto.EduTeacherDTO;
import com.novedu.nov.edu.entity.dto.UserBindTeacherForm;

import javax.servlet.http.HttpServletResponse;
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

    BaseResult<List<EduTeacher>> queryTeacherPage(Page page, EduTeacherDTO teacher);

    BaseResult removeTeacher(String id);

    BaseResult saveTeacher(EduTeacher teacher);

    BaseResult editTeacher(EduTeacher teacher);

    BaseResult<EduTeacher> findTeacherOne(String id);

    BaseResult<List<EduTeacher>> findAll();

    BaseResult<List<EduTeacher>> getClientTeacherList();

    void exportTeacherPage(HttpServletResponse response, Page page, EduTeacherDTO teacher);

    void exportAll(HttpServletResponse response);

    BaseResult queryAllAndHadBind(String id);

    BaseResult updateBindTeacher(UserBindTeacherForm bindTeacherForm);

    BaseResult clearBind(String uid);
}
