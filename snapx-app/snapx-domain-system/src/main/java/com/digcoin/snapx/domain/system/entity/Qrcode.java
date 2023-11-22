package com.digcoin.snapx.domain.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digcoin.snapx.core.common.enums.Operator;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户生成二维码记录
 */
@Data
@TableName("sys_qrcode")
public class Qrcode {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 会员主键
     */
    private Long memberId;

    /**
     * 二维码类型枚举：INVITE 邀请码
     * @see com.digcoin.snapx.domain.system.constant.QrcodeType
     */
    private String codeType;

    /**
     * 使用次数限制。0表示无限制
     */
    private Integer useLimit;

    /**
     * 已使用次数
     */
    private Integer usedCount;

    /**
     * 二维码宽度
     */
    private Integer width;

    /**
     * 图片类型：例如PNG,JPEG
     */
    private String imageType;

    /**
     * 二维码包含的文本内容
     */
    private String content;

    /**
     * base64格式二维码
     */
    private String codeBase64;

    /**
     * 核销完成时间，当usedCount >= useLimit时的时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime writeOffTime;

    /**
     * 操作用户类型 MEMBER 应用端用户，ADMIN 管理端用户，SYSTEM 系统操作
     */
    private Operator operator;

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
