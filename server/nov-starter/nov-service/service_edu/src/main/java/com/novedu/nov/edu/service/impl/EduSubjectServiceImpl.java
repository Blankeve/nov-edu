package com.novedu.nov.edu.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novedu.nov.common.api.BaseResult;
import com.novedu.nov.common.util.ExcelUtils;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.edu.entity.EduSubject;
import com.novedu.nov.edu.mapper.EduSubjectMapper;
import com.novedu.nov.edu.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author juam
 * @since 2021-12-17
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public BaseResult<Map> getSubjects() {
        List<EduSubject> eduSubjects = list().stream().sorted(Comparator.comparingInt(EduSubject::getId)).collect(Collectors.toList());
        Map result = new HashMap();
        result.put("subjects", TreeUtils.toTree(eduSubjects, EduSubject.class));
        int size = eduSubjects.size();
        int lastId = size > 0 ? eduSubjects.get(size - 1).getId() + 1 : 1000;
        result.put("lastId", lastId);
        return BaseResult.success(result);
    }

    @Override
    public BaseResult addSubjects(List<EduSubject> subjects) {
        saveBatch(subjects);
        return BaseResult.success();
    }

    @Override
    public BaseResult removeSubjects(List<EduSubject> subjects) {
        subjects.forEach(o -> removeById(o));
        return BaseResult.success();
    }

    @Override
    public BaseResult exportSubjects(HttpServletResponse response) {
//        List <EduSubject>  eduSubjectVoList= new ArrayList<>();
//        eduSubjectVoList.add(new EduSubject());
//        try {
//            ExcelUtils.defaultExport("所有科目","所有科目",EduSubject.class,eduSubjectVoList,response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return BaseResult.success("导出成功");
    }

    @Override
    public BaseResult updateSubjects(Map<String, List<EduSubject>> eduSubjects) {
        List<EduSubject> eduSubjectList = eduSubjects.get("eduSubjects");
        List<EduSubject> subjects = list().stream().sorted(Comparator.comparingInt(EduSubject::getId)).collect(Collectors.toList());
        List<EduSubject> updateSubjects;
        try {
            updateSubjects = (List<EduSubject>) TreeUtils.toCollection(eduSubjectList, EduSubject.class);
            Map<String, Collection<EduSubject>> result = TreeUtils.checkChangedNodes(subjects, updateSubjects, EduSubject.class);
            List<EduSubject> addList = (List<EduSubject>) result.get("insertNodes");
            List<EduSubject> removeList = (List<EduSubject>) result.get("removeNodes");
            List<EduSubject> changeList = (List<EduSubject>) result.get("updateNodes");
            if (addList != null && addList.size() > 0)
                addSubjects(addList);
            if (removeList != null && removeList.size() > 0)
                removeSubjects(removeList);
            if (changeList != null && changeList.size() > 0) {
                updateBatchById(changeList);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return BaseResult.success();
    }


}
