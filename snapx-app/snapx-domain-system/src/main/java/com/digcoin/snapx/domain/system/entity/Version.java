package com.digcoin.snapx.domain.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.system.constant.Platform;
import com.digcoin.snapx.domain.system.constant.VersionType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/13 16:09
 * @description
 */
@TableName("sys_app_version")
@Data
public class Version {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 应用平台
     */
    private Platform platform;

    /**
     * 版本号 格式：点号分隔的整数，例：1.0.0
     */
    private String versionNumber;

    /**
     * 自增整数版本号
     */
    private Integer versionNumberInt;

    /**
     * 版本更新类型
     */
    private VersionType versionType;

    /**
     * 版本说明
     */
    private String versionDescription;

    /**
     * 版本上线时间，不填立刻推送
     */
    private LocalDateTime onlineTime;

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
