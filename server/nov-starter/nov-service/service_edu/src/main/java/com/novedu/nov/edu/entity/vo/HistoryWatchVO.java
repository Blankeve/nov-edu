package com.novedu.nov.edu.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * @author ：juam
 * @date ：2021/12/21 9:18
 * @description：
 * @modified By：
 * @version:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "EduCourseInfoVO对象", description = "课程信息")
public class HistoryWatchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程讲师ID")
    private Long teacherId;

    @ApiModelProperty(value = "课程讲师")
    private String teacherName;

    private String teacherAvatar;

    private String teacherCareer;


    @ApiModelProperty(value = "课程ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long courseId;

    @ApiModelProperty(value = "课程标题")
    private String courseTitle;


    @ApiModelProperty(value = "课程封面图片路径")
    private String courseCover;


    @ApiModelProperty(value = "章节标题")
    private String chapterTitle;

    @ApiModelProperty(value = "小节id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long videoId;

    @ApiModelProperty(value = "小节标题")
    private String videoTitle;

    @ApiModelProperty(value = "排序字段")
    private Integer chapterSort;

    @ApiModelProperty(value = "排序字段")
    private Integer videoSort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryWatchVO that = (HistoryWatchVO) o;
        return courseId.equals(that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
