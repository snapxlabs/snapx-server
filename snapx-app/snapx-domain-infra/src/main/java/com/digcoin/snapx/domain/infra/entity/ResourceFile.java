package com.digcoin.snapx.domain.infra.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inf_resource_file")
public class ResourceFile {

    @TableId
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件扩展名
     */
    private String fileExtension;

    /**
     * 文件大小，字节数
     */
    private Long fileSize;

    /**
     * 文件的http协议Content-Type
     */
    private String contentType;

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
