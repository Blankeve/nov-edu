package com.novedu.nov.edu.entity;

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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 课程
 * </p>
 *
 * @author juam
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "EduCourse对象", description = "课程")
public class EduCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程ID")
    @TableId(value = "id", type = IdType.NONE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程讲师ID")
    private Long teacherId;

    @TableField(exist = false)
    @ApiModelProperty(value = "课程讲师")
    private String teacherName;

    @TableField(exist = false)
    @ApiModelProperty(value = "讲师生涯")
    private String teacherCareer;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程专业ID")
    private Integer subjectId;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "销售数量")
    private Integer buyCount;

    @ApiModelProperty(value = "报名数量")
    private Integer applyCount;

    @ApiModelProperty(value = "播放数量")
    private Long viewCount;

    @TableField(exist = false)
    @ApiModelProperty(value = "评价数量")
    private Long commentCount;

    @ApiModelProperty(value = "乐观锁")
    private Long version;

    @ApiModelProperty(value = "课程状态 0未发布  1已发布")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除 1删除 0未删除")
    private Integer isDeleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField(exist = false)
    private List<EduChapter> children;
}
