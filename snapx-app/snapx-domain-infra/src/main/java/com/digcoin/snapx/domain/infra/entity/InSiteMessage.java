package com.digcoin.snapx.domain.infra.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.domain.infra.enums.InSiteMessageSubject;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inf_in_site_message")
public class InSiteMessage {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 站内信主题枚举：点赞 THUMBS_UP；NEW_COMMENT；邀请码被使用 FRIEND_SIGNED_UP
     */
    private InSiteMessageSubject subject;

    /**
     * 标题
     */
    private String title;

    /**
     * 正文
     */
    private String context;

    /**
     * 关联的id
     */
    private String relateId;

    /**
     * 关联的JSON对象
     */
    private String payload;

    /**
     * 接收人会员id
     */
    private Long toMemberId;

    /**
     * 状态值：0 未读；1已读
     */
    private Integer status;

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
