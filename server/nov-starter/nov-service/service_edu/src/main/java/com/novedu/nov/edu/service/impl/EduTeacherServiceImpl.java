package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.constants.RedisKeyConstants;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.edu.client.OpenUcenterService;
import com.novedu.nov.edu.entity.EduTeacher;
import com.novedu.nov.edu.entity.dto.EduTeacherDTO;
import com.novedu.nov.edu.entity.dto.UserBindTeacherForm;
import com.novedu.nov.edu.mapper.EduTeacherMapper;
import com.novedu.nov.edu.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    OpenUcenterService openUcenterService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public IPage<List<EduTeacher>> queryTeacherPage(Page page, EduTeacherDTO teacher) {
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
        return page(page, queryWrapper);
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
        Long uid = RequestUtils.getUid();
        BaseResult baseResult = openUcenterService.queryUserRole(Long.valueOf(uid));
        if (baseResult == null) {
            return BaseResult.success();
        }
        Map role = (Map) baseResult.getData();
        Integer code = (Integer) role.get("code");
        if (code == RoleType.TEACHER.getCode()) {
            Long teacherId = query().eq("uid", uid).one().getId();
            return BaseResult.success(query().eq("id", teacherId).list());
        }
        return BaseResult.success(list());
    }

    @Override
    public BaseResult<List<EduTeacher>> getClientTeacherList() {
        List<EduTeacher> teachers;
        if (redisTemplate.hasKey(RedisKeyConstants.CLIENT_TEACHER_LIST)) {
            teachers = (List<EduTeacher>) redisTemplate.opsForValue().get(RedisKeyConstants.CLIENT_TEACHER_LIST);
        } else {
            teachers = lambdaQuery().orderByDesc(EduTeacher::getSort).last("limit 4").list();
            redisTemplate.opsForValue().set(RedisKeyConstants.CLIENT_TEACHER_LIST, teachers, 30, TimeUnit.MINUTES);
        }
        return BaseResult.success(teachers);
    }

    @Override
    public void exportTeacherPage(HttpServletResponse response, EduTeacherDTO teacher) {
        ExcelUtils.exportExcel(queryTeacherPage(new Page(1, -1), teacher).getRecords(), "讲师信息", "讲师信息", EduTeacher.class, "讲师信息", response);
    }

    @Override
    public BaseResult queryAllAndHadBind(String uid) {
        List<EduTeacher> teachers = list();
        String id = "";
        for (EduTeacher teacher : teachers) {
            if (uid.equals(teacher.getUid() + "")) {
                id = teacher.getId() + "";
                break;
            }
        }
        return BaseResult.success().map("list", teachers).map("bind", id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult updateBindTeacher(UserBindTeacherForm bindTeacherForm) {
        EduTeacher teacher = query().eq("id", bindTeacherForm.getId()).one();
        if (teacher.getUid() != null) {
            return BaseResult.error("该讲师已经绑定其它账号");
        }
        clearBind(String.valueOf(bindTeacherForm.getUid()));
        teacher.setUid(bindTeacherForm.getUid());
        return BaseResult.successOrError(updateById(teacher));
    }

    @Override
    public BaseResult clearBind(String uid) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("uid", uid);
        updateWrapper.set("uid", null);
        return BaseResult.successOrError(update(updateWrapper));
    }


}
