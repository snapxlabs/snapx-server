package com.digcoin.snapx.domain.restaurant.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/11 13:29
 * @description
 */
@Data
public class ReviewChangeCountBO implements Serializable {

    @TableField("review_id")
    private Long reviewId;

    @TableField("count")
    private Long count;

}
