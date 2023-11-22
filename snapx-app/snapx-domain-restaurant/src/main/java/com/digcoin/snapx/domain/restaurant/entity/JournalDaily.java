package com.digcoin.snapx.domain.restaurant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 餐厅日记 - 每日汇总
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@TableName("rst_journal_daily")
public class JournalDaily implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 会员 ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 日期
     */
    @TableField("date")
    private LocalDate date;

    /**
     * 年份
     */
    @TableField("year")
    private Long year;

    /**
     * 月份
     */
    @TableField("month")
    private Long month;

    /**
     * 日
     */
    @TableField("day")
    private Long day;

    /**
     * 今天日记总数
     */
    @TableField("total_number")
    private Long totalNumber;

    /**
     * 今天总共已赚
     */
    @TableField("total_earned")
    private BigDecimal totalEarned;

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
     * 创建人 ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人 ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 删除时间，值为0则未删除
     */
    @TableField("deleted")
    @TableLogic
    private Long deleted;

    public static final String ID = "id";


}
