package com.digcoin.snapx.domain.member.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.core.common.enums.Gender;
import com.digcoin.snapx.domain.member.constant.MemberIdentity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mem_member")
public class Member {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 手机号区域代码
     */
    private String phoneAreaCode;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL地址
     */
    private String avatar;

    /**
     * 用户主页背景图地址
     */
    private String avatarCover;

    /**
     * 性别枚举：MALE 男性；FEMALE 女性；UNKNOWN 未知；
     */
    private Gender gender;

    /**
     * 会员身份：GENERAL 普通会员；AGENT 代理人
     */
    private MemberIdentity identity;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 代币钱包地址
     */
    private String wallets;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 可扩展业务状态标志位
     * @see com.digcoin.snapx.domain.member.constant.MemberFlag
     */
    private Long memberFlag;

    /**
     * 最后一次访问时间
     */
    private LocalDateTime lastAccessTime;

    /**
     * 账户是否冻结：0 否； 1是；
     */
    private Boolean freeze;

    /**
     * 账号冻结原因
     */
    private String freezeReason;

    /**
     * 总共已赚
     */
    private BigDecimal totalEarned;

    /**
     * 连续获得积分天数
     */
    private Integer steak;

    /**
     * 连续获得积分开始时间
     */
    private LocalDateTime steakAt;

    /**
     * 连续获得积分天数值失效时间
     */
    private LocalDateTime steakExpireAt;

    /**
     * 用户最后一次访问所在的时区，东时区是正数，西时区是负数，例如东八区：timezone=8，西五区：timezone=-5
     */
    private Integer timezone;

    /**
     * 是否虚拟用户：0 否；1 是；
     */
    private Boolean virtualUser;

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
     * 删除时间，值为0则未删除
     */
    private Long deleted;


}
