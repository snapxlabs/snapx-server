package com.digcoin.snapx.domain.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_material_gallery")
public class MaterialGallery {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 素材类型枚举：IMAGE 图片；
     * @see com.digcoin.snapx.domain.system.constant.MaterialGalleryType
     */
    private String materialType;

    /**
     * 素材分组枚举：AVATAR 头像；AVATAR_COVER 头像对应封面图
     * @see com.digcoin.snapx.domain.system.constant.MaterialGalleryGroup
     */
    private String materialGroup;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 资源文件id
     */
    private Long resourceFileId;

    /**
     * 资源文件url
     */
    private String resourceFileUrl;

    /**
     * 排序号，默认值1000，步进100
     */
    private Integer sort;

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
