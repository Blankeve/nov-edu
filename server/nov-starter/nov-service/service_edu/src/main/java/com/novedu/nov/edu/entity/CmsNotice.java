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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 广告推荐
 * </p>
 *
 * @author juam
 * @since 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CmsNotice对象", description="广告推荐")
public class CmsNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @NotEmpty(message = "公告标题不能为空")
    @ApiModelProperty(value = "公告标题")
    private String title;

    @NotEmpty(message = "公告内容不能为空")
    @ApiModelProperty(value = "公告内容")
    private String content;

    @NotEmpty(message = "发送人不能为空")
    @ApiModelProperty(value = "发送人")
    private String sendUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDeleted;

    @NotNull(message = "公告类型不能为空")
    @ApiModelProperty(value = "公告类型")
    private Integer type;
}
