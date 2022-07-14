package com.novedu.nov.order.entity.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.novedu.nov.common.base.QueryEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author juam
 * @since 2022-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TradeOrder对象", description="订单")
public class TradeOrderDTO extends QueryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "订单号", height = 20, width = 30, isImportField = "true_st",orderNum = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "订单号")
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @Excel(name = "购买课程", height = 20, width = 30, isImportField = "true_st",orderNum = "4")
    @ApiModelProperty(value = "购买课程")
    private String courseTitle;

    @ApiModelProperty(value = "课程封面")
    private String courseCover;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "讲师id")
    private Long teacherId;

    @Excel(name = "课程讲师", height = 20, width = 30, isImportField = "true_st",orderNum = "4")
    @ApiModelProperty(value = "课程讲师")
    private String teacherName;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "会员id")
    private Long uid;

    @Excel(name = "用户昵称", height = 20, width = 30, isImportField = "true_st",orderNum = "2")
    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @TableField(exist = false)
    @Excel(name = "用户名", height = 20, width = 30, isImportField = "true_st",orderNum = "2")
    @ApiModelProperty(value = "用户名")
    private String username;

    @Excel(name = "用户手机", height = 20, width = 30, isImportField = "true_st",orderNum = "3")
    @ApiModelProperty(value = "用户手机")
    private String mobile;

    @Excel(name = "订单金额", height = 20, width = 30, isImportField = "true_st",orderNum = "5")
    @ApiModelProperty(value = "订单金额（分）")
    private BigDecimal totalFee;

    @Excel(name = "支付方式", height = 20, width = 30,replace = {"支付宝_2", "微信_1"}, isImportField = "true_st",orderNum = "7")
    @ApiModelProperty(value = "支付方式（1：微信 2：支付宝）")
    private Integer payType;

    @Excel(name = "课程状态", height = 20, width = 30,replace = {"未支付_0", "已支付_1"}, isImportField = "true_st",orderNum = "6")
    @ApiModelProperty(value = "订单状态（0：未支付 1：已支付）")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @Excel(name = "下单时间", height = 20, width = 30, isImportField = "true_st",exportFormat = "yyyy-MM-dd HH:mm:ss",orderNum = "8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "下单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "支付时间")
    private Date paidTime;

}
