package com.digcoin.snapx.domain.member.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.member.bo.InviteeCountMap;
import com.digcoin.snapx.domain.member.constant.MemberIdentity;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.entity.MemberInvitation;
import com.digcoin.snapx.domain.member.mapper.MemberInvitationMapper;
import com.digcoin.snapx.domain.member.mapper.MemberMapper;
import com.digcoin.snapx.domain.system.entity.SystemSetting;
import com.digcoin.snapx.domain.system.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberInvitationService {

    private final MemberInvitationMapper memberInvitationMapper;
    private final MemberMapper memberMapper;
    private final SystemSettingService systemSettingService;

    public void createInvitation(Long inviterMemberId, Long inviteeMemberId) {
        Long count = memberInvitationMapper.selectCount(Wrappers.lambdaQuery(MemberInvitation.class)
                .eq(MemberInvitation::getInviteeMemberId, inviteeMemberId));
        if (count > 0) {
            return;
        }

        Member member = memberMapper.selectById(inviterMemberId);
        if (Objects.isNull(member)) {
            return;
        }

        MemberInvitation memberInvitation = new MemberInvitation();
        memberInvitation.setInviterMemberId(inviterMemberId);
        memberInvitation.setInviteeMemberId(inviteeMemberId);
        memberInvitation.setIdentity(member.getIdentity());
        if (MemberIdentity.AGENT.equals(member.getIdentity())) {
            SystemSetting systemSetting = systemSettingService.findSystemSetting();
            memberInvitation.setPointsSharingRatio(systemSetting.getPointsSharingRatio());
            memberInvitation.setUsdcSharingRatio(systemSetting.getUsdcSharingRatio());
        }
        memberInvitationMapper.insert(memberInvitation);
    }

    public void updateInvitationGifCount(Long inviterMemberId, Long inviteeMemberId, Long inviterGiftCount, Long inviteeGiftCount) {
        memberInvitationMapper.update(new MemberInvitation(), Wrappers.lambdaUpdate(MemberInvitation.class)
                .eq(MemberInvitation::getInviterMemberId, inviterMemberId)
                .eq(MemberInvitation::getInviteeMemberId, inviteeMemberId)
                .set(MemberInvitation::getInviterGiftCount, inviterGiftCount)
                .set(MemberInvitation::getInviteeGiftCount, inviteeGiftCount));
    }

    public MemberInvitation findMemberInvitationByInvitee(Long inviteeMemberId) {
        return memberInvitationMapper.selectOne(Wrappers.lambdaQuery(MemberInvitation.class)
                .eq(MemberInvitation::getInviteeMemberId, inviteeMemberId));
    }

    public Map<Long, Long> getInviteeCountMap(Collection<Long> inviterMemberIds) {
        if (CollectionUtils.isEmpty(inviterMemberIds)) {
            return Collections.emptyMap();
        }
        return memberInvitationMapper.getInviteeCountMap(inviterMemberIds)
                .stream()
                .collect(Collectors.toMap(InviteeCountMap::getInviterMemberId, InviteeCountMap::getInviteeCount));
    }

    public List<MemberInvitation> getInvitationByInviterMemberIds(Long inviterMemberId) {
        return memberInvitationMapper.selectList(Wrappers.lambdaQuery(MemberInvitation.class).eq(MemberInvitation::getInviterMemberId, inviterMemberId));
    }

    public List<MemberInvitation> listMemberInvitation(Collection<Long> inviteeMemberIds) {
        if (CollectionUtils.isEmpty(inviteeMemberIds)) {
            return Collections.emptyList();
        }
        return memberInvitationMapper.selectList(Wrappers.lambdaQuery(MemberInvitation.class)
                .in(MemberInvitation::getInviteeMemberId, inviteeMemberIds));
    }

    public Map<Long, Member> mappingInviterMember(Collection<Long> inviteeMemberIds) {
        if (CollectionUtils.isEmpty(inviteeMemberIds)) {
            return Collections.emptyMap();
        }
        List<MemberInvitation> invitationList = listMemberInvitation(inviteeMemberIds);
        if (CollectionUtils.isEmpty(invitationList)) {
            return Collections.emptyMap();
        }
        Set<Long> memberIds = invitationList.stream().map(MemberInvitation::getInviterMemberId).collect(Collectors.toSet());
        List<Member> memberList = memberMapper.selectList(Wrappers.lambdaQuery(Member.class).in(Member::getId, memberIds));
        if (CollectionUtils.isEmpty(memberList)) {
            return Collections.emptyMap();
        }
        Map<Long, Member> memberMap = memberList.stream().collect(Collectors.toMap(Member::getId, Function.identity()));
        Map<Long, Member> result = new HashMap<>(invitationList.size());
        for (MemberInvitation invitation : invitationList) {
            result.put(invitation.getInviteeMemberId(), memberMap.get(invitation.getInviterMemberId()));
        }
        return result;
    }
}
