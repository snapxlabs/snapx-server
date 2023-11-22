package com.digcoin.snapx.domain.member.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.constant.Common;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.member.bo.MemberActivityQueryBO;
import com.digcoin.snapx.domain.member.entity.MemberActivity;
import com.digcoin.snapx.domain.member.mapper.MemberActivityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/4 0:17
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberActivityService {

    private final MemberActivityMapper memberActivityMapper;

    public MemberActivity saveMemberActivity(MemberActivity memberActivity) {
        memberActivityMapper.insert(memberActivity);
        return memberActivity;
    }


    public MemberActivity getMemberActivity(Long memberId, Long activityId) {
        return memberActivityMapper.selectOne(Wrappers.lambdaQuery(MemberActivity.class)
                .eq(MemberActivity::getMemberId, memberId)
                .eq(MemberActivity::getActivityId, activityId));
    }

    public PageResult<MemberActivity> pageMemberActivity(MemberActivityQueryBO queryBO) {
        QueryWrapper<MemberActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mma.deleted", Common.ZERO)
                .like(StringUtils.isNotBlank(queryBO.getMemberAccount()), "mm.account", queryBO.getMemberAccount())
                .like(StringUtils.isNotBlank(queryBO.getActivityName()), "sa.name", queryBO.getActivityName())
                .last("order by mma.create_time desc ");
        return PageResult.fromPage(memberActivityMapper.pageMemberActivity(PageHelper.getPage(queryBO), queryWrapper), Function.identity());
    }

    public IPage<MemberActivity> pageAppMemberActivity(MemberActivityQueryBO queryBO) {
        QueryWrapper<MemberActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberActivity::getMemberId, queryBO.getMemberId())
                .orderByDesc(MemberActivity::getCreateTime);
        return memberActivityMapper.pageMemberActivity(PageHelper.getPage(queryBO), queryWrapper);
    }
}
