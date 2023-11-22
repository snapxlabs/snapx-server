package com.digcoin.snapx.domain.infra.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("inf_aws_sns_endpoint")
@Data
public class AwsSnsEndpoint {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 在AWS SNS 创建平台应用代号：SNAPX_APP_IOS iOS应用端；SNAPX_APP_ANDROID 安卓应用端
     */
    private String platform;

    /**
     * 在AWS SNS 创建平台应用id
     */
    private String platformAppArn;

    /**
     * 移动推送服务渠道枚举 ADM：Amazon Device Messaging；APNS：iOS推送渠道 BAIDU：百度云推送；FCM：Firebase Cloud Messaging；MPNS：适用于 Windows Phone 的 Microsoft 推送通知服务；WNS：Windows 推送通知服务；
     * @see com.digcoin.snapx.domain.infra.constant.AwsSnsPushChannel
     */
    private String pushChannel;

    /**
     * 移动设备token或设备id，和由移动推送服务渠道提供
     */
    private String deviceToken;

    /**
     * AWS SNS 服务设备标识，代表一个设备上的一个应用
     */
    private String endpointArn;

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
