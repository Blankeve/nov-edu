package com.novedu.nov.edu.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ：juam
 * @date ：2021/12/27 15:25
 * @description：
 * @modified By：
 * @version:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "EduCourse对象", description = "课程")
public class EduCourseInfoDTO {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程ID")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @NotNull(message = "课程讲师不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程讲师ID")
    private Long teacherId;

    @ApiModelProperty(value = "课程专业ID")
    private Integer[] subjectId;

    private Integer clientSubjectId;

    private Integer orderFieldValue;

    @NotEmpty(message = "课程标题不能为空")
    @ApiModelProperty(value = "课程标题")
    private String title;

    @NotNull(message = "课程销售价格不能为空")
    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @NotNull(message = "总课时不能为空")
    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @NotEmpty(message = "课程封面图片路径不能为空")
    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "销售数量")
    private Integer buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;

    @ApiModelProperty(value = "乐观锁")
    private Long version;

    @ApiModelProperty(value = "课程状态 0未发布  1已发布")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除 1删除 0未删除")
    private Integer isDeleted;

    @NotEmpty(message = "课程简介不能为空")
    @ApiModelProperty(value = "课程简介")
    private String description;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    private Date endTime;

    public enum ORDER_BY{NONE,NEWEST_ASC,NEWEST_DESC,PRICE_ASC,PRICE_DESC}
}
