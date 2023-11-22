package com.digcoin.snapx.domain.camera.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.digcoin.snapx.domain.camera.entity.CameraMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 15:24
 * @description
 */
@Mapper
public interface CameraMemberMapper extends BaseMapper<CameraMember> {

    @Select("SELECT ccm.*, mm.account AS memberAccount, mm.nickname AS memberNickName, " +
            "cc.name AS cameraName, cc.code AS cameraCode FROM `cam_camera_member` AS ccm " +
            "LEFT JOIN `cam_camera` AS cc ON cc.id = ccm.camera_id " +
            "LEFT JOIN `mem_member` AS mm ON mm.id = ccm.member_id " +
            "${ew.customSqlSegment}")
    IPage<CameraMember> pageCameraMember(IPage<CameraMember> page,@Param(Constants.WRAPPER) QueryWrapper<CameraMember> queryWrapper);

}
