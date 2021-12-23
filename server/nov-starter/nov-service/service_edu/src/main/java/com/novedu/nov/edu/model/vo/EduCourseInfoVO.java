package com.novedu.nov.edu.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author ：juam
 * @date ：2021/12/21 9:18
 * @description：
 * @modified By：
 * @version:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EduCourseInfoVO对象", description="课程信息")
public class EduCourseInfoVO {

    @ApiModelProperty(value = "课程讲师ID")
    private Long teacherId;

    @ApiModelProperty(value = "课程专业ID")
    private Integer[] subjectId;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "课程简介")
    private String description;
}
