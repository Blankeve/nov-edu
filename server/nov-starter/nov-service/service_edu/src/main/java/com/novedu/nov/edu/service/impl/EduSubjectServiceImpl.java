package com.novedu.nov.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.novedu.nov.common.base.BaseResult;
import com.novedu.nov.common.base.RoleType;
import com.novedu.nov.common.entity.UserDTO;
import com.novedu.nov.common.util.RequestUtils;
import com.novedu.nov.common.util.TreeUtils;
import com.novedu.nov.edu.client.OpenOrderService;
import com.novedu.nov.edu.client.OpenStatisticsService;
import com.novedu.nov.edu.client.OpenUcenterService;
import com.novedu.nov.edu.entity.EduCourse;
import com.novedu.nov.edu.entity.EduSubject;
import com.novedu.nov.edu.entity.EduTeacher;
import com.novedu.nov.edu.entity.vo.DashBoardInfoVO;
import com.novedu.nov.edu.mapper.EduSubjectMapper;
import com.novedu.nov.edu.service.EduCourseService;
import com.novedu.nov.edu.service.EduSubjectService;
import com.novedu.nov.edu.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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

    @Autowired
    EduCourseService courseService;
    @Autowired
    OpenUcenterService openUcenterService;
    @Autowired
    EduTeacherService teacherService;
    @Autowired
    OpenOrderService openOrderService;
    @Autowired
    OpenStatisticsService openStatisticsService;


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

    public BaseResult<List<Integer>> getParentSubjects(Integer id) {
        List<EduSubject> eduSubjects = list();
        List<Integer> nodes = new ArrayList<>();
        nodes.add(id);
        for (EduSubject o : eduSubjects) {
            if (o.getId().equals(id)) {
                addParentSubject(nodes, eduSubjects, o.getParentId());
                break;
            }
        }
        Collections.reverse(nodes);
        return BaseResult.success(nodes);
    }

    private void addParentSubject(List<Integer> list, List<EduSubject> eduSubjects, Integer pid) {
        if (pid == null || pid == 0)
            return;
        list.add(pid);
        for (EduSubject eduSubject : eduSubjects) {
            if (eduSubject.getId().equals(pid)) {
                addParentSubject(list, eduSubjects, eduSubject.getParentId());
            }
        }
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

    @Override
    public BaseResult getSubjectRatios() {
        //1.课程分类详情     当前分类课程数量/所以分类课程数
        DashBoardInfoVO dashBoardInfoVO = new DashBoardInfoVO();
        List<EduCourse> courses = courseService.list();
        List<EduSubject> subjects = list();
        Map subjectRatios = new HashMap();
        for (EduSubject subject : subjects) {
            if (subject.getParentId() != null && subject.getParentId() != 0) {
                int count = 0;
                for (EduCourse course : courses) {
                    if (course.getSubjectId().equals(subject.getId())) {
                        count++;
                    }
                }
                subjectRatios.put(subject.getTitle(), new BigDecimal((float) count / courses.size() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }
        }
        List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String, Float>>(subjectRatios.entrySet());
        //根据value排序
        Collections.sort(list, (o1, o2) -> (int) (o2.getValue() - o1.getValue()));
        if (list.size() > 8)
            list = list.subList(0, 8);
        List<Map> mapList = new ArrayList<>();
        for (Map.Entry<String, Float> stringFloatEntry : list) {
            Map map = new HashMap();
            map.put("title", stringFloatEntry.getKey());
            map.put("value", stringFloatEntry.getValue());
            mapList.add(map);
        }
        dashBoardInfoVO.setSubjectRatios(mapList);
        return BaseResult.success(dashBoardInfoVO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult saveOrUpdateSubject(EduSubject subject) {
        List<EduSubject> eduSubjectList = list();
        List<EduSubject> repeatSubjects = eduSubjectList.stream().filter(e -> e.getTitle().equals(subject.getTitle())).collect(Collectors.toList());
        if (repeatSubjects.size() > 0) {
            if (!(repeatSubjects.size() == 1 && repeatSubjects.stream().filter(e -> e.getId().equals(subject.getId())).count() > 0))
                return BaseResult.error("分类名称《" + subject.getTitle() + "》已存在");
        }
        List<EduSubject> parents = new ArrayList<>();
        parents = findParents(eduSubjectList, parents, subject.getParentId());
        if (parents.size() > 1) {
            return BaseResult.error("暂时不支持二级以上分类");
        }
        return BaseResult.successOrError(saveOrUpdate(subject));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResult removeSubject(Integer id) {
        List<EduSubject> eduSubjectList = list();
        List<EduSubject> children = new ArrayList<>();
        List<EduCourse> courses = courseService.list();
        children = findChildren(eduSubjectList, children, id);
        //删除一级分类下所有课程
        if (children.size() > 0) {
            for (EduSubject child : children) {
                for (EduCourse course : courses) {
                    if (course.getSubjectId().equals(child.getId())) {
                        courseService.removeCourse(course.getId());
                    }
                }
                removeById(child);
            }
        }
        //删除二级分类下所有课程
        else {
            for (EduCourse course : courses) {
                if (course.getSubjectId().equals(id)) {
                    courseService.removeCourse(course.getId());
                }
            }
        }
        removeById(id);
        return BaseResult.success();
    }

    public List<EduSubject> findChildren(List<EduSubject> all, List<EduSubject> children, Integer id) {
        for (EduSubject eduSubject : all) {
            if (eduSubject.getParentId().equals(id)) {
                children.add(eduSubject);
            }
        }
        return children;
    }

    public List<EduSubject> findParents(List<EduSubject> all, List<EduSubject> parents, Integer id) {
        for (EduSubject eduSubject : all) {
            if (eduSubject.getId().equals(id)) {
                Integer parentId = eduSubject.getParentId();
                parents.add(eduSubject);
                if (parentId != null && parentId != 0) {
                    findParents(all, parents, parentId);
                } else
                    return parents;
            }
        }
        return parents;
    }

}
