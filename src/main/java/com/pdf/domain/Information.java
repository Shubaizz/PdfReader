package com.pdf.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName information
 */
@TableName(value ="information")
@Data
@Builder
public class Information implements Serializable {
    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 纳税人名称
     */
    @TableField(value = "name")
            private String name;

    /**
     * 填表日期
     */
    @TableField(value = "dataOfFilling")
    private String dataoffilling;

    /**
     * 申报日期
     */
    @TableField(value = "dateOfDeclaration")
    private String dateofdeclaration;

    /**
     * 税款所属时间
     */
    @TableField(value = "taxationPeriod")
    private String taxationperiod;

    /**
     * 纳税人识别号（统一社会信用代码）
     */
    @TableField(value = "number")
    private String number;

    /**
     * 金额单位元至角分
     */
    @TableField(value = "amountUnit")
    private String amountunit;

    /**
     * 表名
     */
    @TableField(value = "tableName")
    private String tablename;

    /**
     * 文件名
     */
    @TableField(value = "FileName")
    private String filename;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}