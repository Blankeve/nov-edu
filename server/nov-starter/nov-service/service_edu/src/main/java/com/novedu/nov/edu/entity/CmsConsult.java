package com.novedu.nov.edu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author juam
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CmsConsult对象", description="")
public class CmsConsult implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "咨询内容不能为空")
    @ApiModelProperty(value = "咨询内容")
    private String content;

    @ApiModelProperty(value = "咨询用户id")
    private Long uid;

    @ApiModelProperty(value = "回复人id")
    private Long adminId;

    @ApiModelProperty(value = "回复内容")
    private String replyContent;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDeleted;


}
