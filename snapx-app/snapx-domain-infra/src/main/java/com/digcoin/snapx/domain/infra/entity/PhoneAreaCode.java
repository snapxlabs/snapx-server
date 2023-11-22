package com.digcoin.snapx.domain.infra.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("inf_phone_area_code")
public class PhoneAreaCode {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 地区英文名
     */
    private String englishName;

    /**
     * 地区中文名
     */
    private String chineseName;

    /**
     * 地区代码
     */
    private String countryCode;

    /**
     * 电话代码
     */
    private String phoneCode;

    /**
     * 国旗emoji
     */
    private String emoji;

    /**
     * 所有字段拼接在一起，方便模糊查询
     */
    private String mix;

}
