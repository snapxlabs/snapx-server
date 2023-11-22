package com.digcoin.snapx.domain.infra.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.infra.enums.NotificationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inf_notification")
public class Notification {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 正文
     */
    private String context;

    /**
     * 描述，备注信息
     */
    private String description;

    /**
     * 是否发送站内信
     */
    private Boolean inSiteMessage;

    /**
     * 是否发送APP推送
     */
    private Boolean push;

    /**
     * 全局推送
     */
    private Boolean global;

    /**
     * 推送用户id集合，global=false时，逗号分隔存储指定用户id
     */
    private String memberIds;

    /**
     * 推送状态： DRAFT 草稿；DONE 已推送
     */
    private NotificationStatus status;

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
