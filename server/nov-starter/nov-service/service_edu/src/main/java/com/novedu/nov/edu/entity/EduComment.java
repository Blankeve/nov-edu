package com.novedu.nov.edu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author juam
 * @since 2022-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="EduComment对象", description="评论")
public class EduComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "讲师ID")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "讲师id")
    private Long teacherId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "会员id")
    private Long uid;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private Integer isDeleted;

    private Integer mark;

    private Integer reported;
}
