package com.digcoin.snapx.domain.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_admin_user")
public class AdminUser {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 重复密码
     */
    @TableField(exist = false)
    private String repeatPassword;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String mail;

    /**
     * 备注
     */
    private String remark;

    /**
     * 停用/启用标识；false 停用；true 启用
     */
    private Boolean enabled;

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
