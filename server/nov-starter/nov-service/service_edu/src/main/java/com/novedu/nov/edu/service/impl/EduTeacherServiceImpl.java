package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.JwtUtils;
import com.novedu.nov.edu.client.UserRoleClient;
import com.novedu.nov.edu.entity.EduTeacher;
import com.novedu.nov.edu.entity.dto.EduTeacherDTO;
import com.novedu.nov.edu.entity.dto.UserBindTeacherForm;
import com.novedu.nov.edu.mapper.EduTeacherMapper;
import com.novedu.nov.edu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

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


    @Autowired
    UserRoleClient userRoleClient;

    @Override
    public BaseResult<List<EduTeacher>> queryTeacherPage(Page page, EduTeacherDTO teacher) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasText(teacher.getName()))
            queryWrapper.like("name", teacher.getName());
        if (teacher.getLevel() != null)
            queryWrapper.eq("level", teacher.getLevel());
        Date start = teacher.getStartTime();
        Date end = teacher.getEndTime();
        if (start != null && end != null && end.getTime() > start.getTime())
            queryWrapper.apply("create_time > date_format({0},'%Y-%m-%d %H:%i:%s') and create_time < date_format({1},'%Y-%m-%d %H:%i:%s')", start, end);
        queryWrapper.orderByDesc("sort");
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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult<List<EduTeacher>> findAll() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader("X-Token");
        String uid = JwtUtils.getAudience(token).get("uid");
        BaseResult baseResult = userRoleClient.queryUserRole(Long.valueOf(uid));
        if (baseResult == null) {
            return BaseResult.success();
        }
        Map role = (Map) baseResult.getData();
        Integer code = (Integer) role.get("code");
        if (code == RoleType.TEACHER.getCode()){
            Long teacherId = query().eq("uid", uid).one().getId();
            return BaseResult.success(query().eq("id",teacherId).list());
        }
        return BaseResult.success(list());
    }

    @Override
    public BaseResult<List<EduTeacher>> getClientTeacherList() {
        return BaseResult.success(query().orderByDesc("sort").last("limit 4").list());
    }

    @Override
    public void exportTeacherPage(HttpServletResponse response, Page page, EduTeacherDTO teacher) {
        BaseResult baseResult = queryTeacherPage(page, teacher);
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "讲师信息", "讲师信息", EduTeacher.class, "讲师信息", response);
        }
    }

    @Override
    public void exportAll(HttpServletResponse response) {
        BaseResult baseResult = queryTeacherPage(new Page(1, count()), new EduTeacherDTO());
        if (baseResult != null && BaseResult.success().getCode().equals(baseResult.getCode())) {
            Page page1 = (Page) baseResult.getData();
            ExcelUtils.exportExcel(page1.getRecords(), "讲师信息", "讲师信息", EduTeacher.class, "讲师信息", response);
        }

    }

    @Override
    public BaseResult queryAllAndHadBind(String uid) {
        List<EduTeacher> teachers = list();
        String id = "";
        for (EduTeacher teacher : teachers) {
            if (uid.equals(teacher.getUid()+"")) {
                id = teacher.getId() + "";
                break;
            }
        }
        return BaseResult.success().mapSet("list", teachers).mapSet("bind", id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult updateBindTeacher(UserBindTeacherForm bindTeacherForm) {
        EduTeacher teacher = query().eq("id", bindTeacherForm.getId()).one();
        if(teacher.getUid() != null){
            return BaseResult.error("该讲师已经绑定其它账号");
        }
        clearBind(String.valueOf(bindTeacherForm.getUid()));
        teacher.setUid(bindTeacherForm.getUid());
        return BaseResult.successOrError(updateById(teacher));
    }

    @Override
    public BaseResult clearBind(String uid) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("uid",uid);
        updateWrapper.set("uid",null);
        return BaseResult.successOrError(update(updateWrapper));
    }


}
