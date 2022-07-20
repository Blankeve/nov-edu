package com.novedu.nov.edu.entity.vo;

import com.novedu.nov.edu.entity.EduCourse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class DashBoardInfoVO {

    private Integer courseCount;

    private List<Map> subjectRatios;

    private List<EduCourse> recentAddCourses;

    private Integer OrderCount;

    private BigDecimal orderAmount;

    private String teacherName;
}
