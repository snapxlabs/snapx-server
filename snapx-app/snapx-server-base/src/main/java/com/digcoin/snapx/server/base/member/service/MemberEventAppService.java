package com.digcoin.snapx.server.base.member.service;

import com.digcoin.snapx.core.redisson.BlockingQueueManager;
import com.digcoin.snapx.domain.member.constant.MemberFlag;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.event.MemberAccessEvent;
import com.digcoin.snapx.domain.member.event.MemberSignUpEvent;
import com.digcoin.snapx.domain.member.service.MemberEventAggregationService;
import com.digcoin.snapx.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventAppService {

    @Qualifier("memberAccessEventQueue")
    private final BlockingQueueManager<MemberAccessEvent> memberAccessEventQueue;

    @Qualifier("memberSignUpEventQueue")
    private final BlockingQueueManager<MemberSignUpEvent> memberSignUpEventQueue;

    private final MemberService memberService;
    private final MemberEventAggregationService memberEventAggregationService;

    public void publishSignUpEvent(Long memberId) {
        try {
            memberSignUpEventQueue.publishEvent(new MemberSignUpEvent(memberId));
        } catch (Exception e) {
            log.error("publishSignUpEvent fail memberId:[{}]", memberId, e);
        }
    }

    public void publishAccessEvent(Long memberId) {
        try {
            memberAccessEventQueue.publishEvent(new MemberAccessEvent(memberId));
        } catch (Exception e) {
            log.error("publishAccessEvent fail memberId:[{}]", memberId, e);
        }
    }

    @EventListener
    public void handleSignUpEvent(MemberSignUpEvent event) {
        Member member = memberService.findMember(event.getMemberId());
        if (Objects.isNull(member)) {
            return;
        }
        memberEventAggregationService.addMemberSignUpCount(event.getSignUpTime());
    }

    @EventListener
    public void handleAccessEvent(MemberAccessEvent event) {
        Member member = memberService.findMember(event.getMemberId());
        if (Objects.isNull(member)) {
            return;
        }
        Member example = new Member();
        example.setId(event.getMemberId());
        example.setLastAccessTime(event.getAccessTime());
        memberService.updateMember(example);
        memberEventAggregationService.addMemberVisitCount(member, event.getAccessTime());
        memberEventAggregationService.addMemberAccessCount(member, event.getAccessTime());
        memberEventAggregationService.addMemberMonthAccessCount(member, event.getAccessTime());
        if (memberService.isNotifyTwoDaysLeave(member)) {
            memberService.updateMemberFlagOff(event.getMemberId(), MemberFlag.NOTIFY_TWO_DAYS_LEAVE);
        }
    }

}
