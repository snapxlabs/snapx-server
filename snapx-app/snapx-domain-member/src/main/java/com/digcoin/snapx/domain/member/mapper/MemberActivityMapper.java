package com.digcoin.snapx.domain.member.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.digcoin.snapx.domain.member.entity.MemberActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/4 0:14
 * @description
 */
@Mapper
public interface MemberActivityMapper extends BaseMapper<MemberActivity> {

    @Select("SELECT mma.*, mm.account AS memberAccount, sa.name AS activityName FROM `mem_member_activity` AS mma " +
            "LEFT JOIN `sys_activity` AS sa ON sa.id = mma.activity_id " +
            "LEFT JOIN `mem_member` AS mm ON mm.id = mma.member_id " +
            "${ew.customSqlSegment}")
    IPage<MemberActivity> pageMemberActivity(IPage<Object> page, @Param(Constants.WRAPPER) QueryWrapper<MemberActivity> queryWrapper);
}
