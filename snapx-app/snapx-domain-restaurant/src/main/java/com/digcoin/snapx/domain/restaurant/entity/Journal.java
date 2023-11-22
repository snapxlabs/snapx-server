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
 * 餐厅日记
 * </p>
 *
 * @author Generator
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@TableName("rst_journal")
public class Journal implements Serializable {

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
     * 餐厅 ID
     */
    @TableField("restaurant_id")
    private Long restaurantId;

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
     * 餐厅评价 ID
     */
    @TableField("review_id")
    private Long reviewId;

    /**
     * 花费
     */
    @TableField("spent")
    private BigDecimal spent;

    /**
     * 货币代码
     */
    @TableField("currency_code")
    private String currencyCode;

    /**
     * 已赚
     */
    @TableField("earned")
    private BigDecimal earned;

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

    public static final String EARNED = "earned";
    public static final String DATE = "date";
    public static final String MEMBER_ID = "member_id";


}
