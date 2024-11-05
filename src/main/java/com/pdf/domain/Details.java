package com.pdf.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName details
 */
@TableName(value ="details")
@Data
@Builder
public class Details implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
        @TableField(value = "informationId")
    private Integer informationid;

    /**
     * 项目
     */
    @TableField(value = "project")
    private String project;

    /**
     * 项目明细
     */
    @TableField(value = "projectDetails")
    private String projectdetails;

    /**
     * 栏次
     */
    @TableField(value = "columnNumber")
    private String columnnumber;

    /**
     * 一般项目本月数
     */
    @TableField(value = "numberOfMoth")
    private String numberofmoth;

    /**
     * 一般项目本年累计
     */
    @TableField(value = "numberOfYear")
    private String numberofyear;

    /**
     * 即征即退项目本月数
     */
    @TableField(value = "immediateWithdrawalMonth")
    private String immediatewithdrawalmonth;

    /**
     * 即征即退项目本年累计
     */
    @TableField(value = "refundProjectsAccumulated")
    private String refundprojectsaccumulated;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}