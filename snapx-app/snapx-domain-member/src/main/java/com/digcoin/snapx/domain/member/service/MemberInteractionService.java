package com.digcoin.snapx.domain.member.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.member.entity.MemberInteraction;
import com.digcoin.snapx.domain.member.mapper.MemberInteractionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 15:42
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberInteractionService {

    private final MemberInteractionMapper memberInteractionMapper;
    private final RedissonClient redissonClient;

    public MemberInteraction findMemberInteractionByMemberId(Long memberId) {
        return memberInteractionMapper.selectOne(Wrappers.<MemberInteraction>lambdaQuery().eq(MemberInteraction::getMemberId, memberId));
    }

    public MemberInteraction saveOrUpdateMemberInteraction(MemberInteraction memberInteraction) {
        MemberInteraction entity = this.findMemberInteractionByMemberId(memberInteraction.getMemberId());
        LocalDateTime followTwitterTime = Boolean.TRUE.equals(memberInteraction.getIsFollowTwitter()) ? LocalDateTime.now() : null;
        LocalDateTime joinDiscordTime = Boolean.TRUE.equals(memberInteraction.getIsJoinDiscord()) ? LocalDateTime.now() : null;
        if (Objects.isNull(entity)) {
            memberInteraction.setFollowTwitterTime(followTwitterTime);
            memberInteraction.setJoinDiscordTime(joinDiscordTime);
            memberInteractionMapper.insert(memberInteraction);
            return memberInteraction;
        }
        entity.setIsFollowTwitter(memberInteraction.getIsFollowTwitter());
        entity.setIsJoinDiscord(memberInteraction.getIsJoinDiscord());
        entity.setFollowTwitterTime(followTwitterTime);
        entity.setJoinDiscordTime(joinDiscordTime);
        memberInteractionMapper.updateById(entity);
        return entity;
    }

    public void updateJoinDiscord(Long memberId) {
        MemberInteraction memberInteraction = ensureMemberInteraction(memberId);
        MemberInteraction example = new MemberInteraction();
        example.setId(memberInteraction.getId());
        example.setIsJoinDiscord(true);
        example.setJoinDiscordTime(LocalDateTime.now());
        memberInteractionMapper.updateById(example);
    }

    public void updateFollowTwitter(Long memberId) {
        MemberInteraction memberInteraction = ensureMemberInteraction(memberId);
        MemberInteraction example = new MemberInteraction();
        example.setId(memberInteraction.getId());
        example.setIsFollowTwitter(true);
        example.setFollowTwitterTime(LocalDateTime.now());
        memberInteractionMapper.updateById(example);
    }

    private MemberInteraction ensureMemberInteraction(Long memberId) {
        MemberInteraction memberInteraction = memberInteractionMapper.selectOne(
                Wrappers.lambdaQuery(MemberInteraction.class)
                        .eq(MemberInteraction::getMemberId, memberId));
        if (Objects.nonNull(memberInteraction)) {
            return memberInteraction;
        }

        RLock lock = redissonClient.getLock("SNAPX:RL:MEM:INT:" + memberId);
        try {
            lock.lock(1500, TimeUnit.MILLISECONDS);
            memberInteraction = memberInteractionMapper.selectOne(
                    Wrappers.lambdaQuery(MemberInteraction.class)
                            .eq(MemberInteraction::getMemberId, memberId));

            if (Objects.isNull(memberInteraction)) {
                memberInteraction = new MemberInteraction();
                memberInteraction.setMemberId(memberId);
                memberInteraction.setIsFollowTwitter(false);
                memberInteraction.setIsJoinDiscord(false);
                memberInteraction.setFollowTwitterTime(null);
                memberInteraction.setJoinDiscordTime(null);
                memberInteractionMapper.insert(memberInteraction);
            }
        } finally {
            if (Objects.nonNull(lock)) {
                lock.unlock();
            }
        }

        return memberInteraction;
    }

}
