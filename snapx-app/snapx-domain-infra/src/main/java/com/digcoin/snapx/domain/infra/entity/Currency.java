package com.digcoin.snapx.domain.infra.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/3 22:21
 * @description
 */
@Data
@Accessors(chain = true)
@TableName("inf_currency")
public class Currency implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 货币名称
     */
    private String name;

    /**
     * 货币代码
     */
    private String code;

    /**
     * 货币符号
     */
    private String symbol;

    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

    /**
     * 创建操作人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新操作人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除时间，值为0则未删除
     */
    private Long deleted;

}
