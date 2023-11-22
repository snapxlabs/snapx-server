package com.digcoin.snapx.server.app.member.schedule;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.infra.entity.AwsSnsEndpoint;
import com.digcoin.snapx.domain.infra.service.AwsSnsMobilePushService;
import com.digcoin.snapx.domain.member.constant.MemberFlag;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.mapper.MemberMapper;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.server.app.member.config.MemberScheduleProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.schedule.push", havingValue = "true")
public class MemberSchedule {

    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final AwsSnsMobilePushService awsSnsMobilePushService;
    private final MemberScheduleProperties memberScheduleProperties;

    @Scheduled(cron = "0 0 * * * *"/*每小时执行一次*/)
    public void checkIfSteakWillLost() {
        // 查找12个小时后steak过期的用户
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.plusHours(12);
        List<Member> willExpiredList = memberMapper.selectList(Wrappers.lambdaQuery(Member.class)
                .gt(Member::getSteakExpireAt, now)
                .le(Member::getSteakExpireAt, expired)
                .last(String.format("and (member_flag & %1$d) != %1$d", MemberFlag.NOTIFY_STEAK_LOST)));
        log.info("checkIfSteakWillLost find {} member to notify", willExpiredList.size());
        if (CollectionUtils.isEmpty(willExpiredList)) {
            return;
        }
        Set<Long> memberIds = willExpiredList.stream().map(Member::getId).collect(Collectors.toSet());
        List<AwsSnsEndpoint> endpointList = awsSnsMobilePushService.getAwsSnsEndpointList(memberIds);
        Map<Long, List<AwsSnsEndpoint>> endpointGroup = endpointList.stream().collect(Collectors.groupingBy(AwsSnsEndpoint::getMemberId));
        log.info("checkIfSteakWillLost find {} member can notify", endpointGroup.size());

        String message = "You are about to lose your streak. Snap now to protect it!";
        for (Map.Entry<Long, List<AwsSnsEndpoint>> entry : endpointGroup.entrySet()) {
            Long memberId = entry.getKey();
            memberService.updateMemberFlagOn(memberId, MemberFlag.NOTIFY_STEAK_LOST);

            List<AwsSnsEndpoint> entryValue = entry.getValue();
            if (CollectionUtils.isEmpty(entryValue)) {
                continue;
            }
            awsSnsMobilePushService.publish(entryValue, message);
        }
    }

    @Scheduled(cron = "0 0 * * * *"/*每小时执行一次*/)
    public void checkWhoLeaveTowDays() {
        // 查找最后一次访问时间是2天之前的用户
        LocalDateTime lastAccessTime = LocalDateTime.now().minusDays(2);
        List<Member> leaveTowDays = memberMapper.selectList(Wrappers.lambdaQuery(Member.class)
                .le(Member::getLastAccessTime, lastAccessTime)
                .last(String.format("and (member_flag & %1$d) != %1$d", MemberFlag.NOTIFY_TWO_DAYS_LEAVE)));
        log.info("checkWhoLeaveTowDays find {} member to notify", leaveTowDays.size());
        if (CollectionUtils.isEmpty(leaveTowDays)) {
            return;
        }
        Set<Long> memberIds = leaveTowDays.stream().map(Member::getId).collect(Collectors.toSet());
        List<AwsSnsEndpoint> endpointList = awsSnsMobilePushService.getAwsSnsEndpointList(memberIds);
        Map<Long, List<AwsSnsEndpoint>> endpointGroup = endpointList.stream().collect(Collectors.groupingBy(AwsSnsEndpoint::getMemberId));
        log.info("checkWhoLeaveTowDays find {} member can notify", endpointGroup.size());

        String message = "Your camera is hungry. It's time to let your camera eat first!";
        for (Map.Entry<Long, List<AwsSnsEndpoint>> entry : endpointGroup.entrySet()) {
            Long memberId = entry.getKey();
            memberService.updateMemberFlagOn(memberId, MemberFlag.NOTIFY_TWO_DAYS_LEAVE);

            List<AwsSnsEndpoint> entryValue = entry.getValue();
            if (CollectionUtils.isEmpty(entryValue)) {
                continue;
            }
            awsSnsMobilePushService.publish(entryValue, message);
        }
    }

    @Scheduled(cron = "0 0 * * * *"/*每小时执行一次*/)
    public void doHourCheck() {
        LocalDateTime now = LocalDateTime.now();
        int timezone = memberScheduleProperties.getTargetHours() - now.getHour();
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(timezone));
        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        DayOfWeek dayOfWeek = currentTime.getDayOfWeek();

        log.info("每小时推送检查：UTC:[{}] hour:[{}] 12pm timezone:[{}] time:[{}]", now, now.getHour(), timezone, currentTime);

        if (!memberScheduleProperties.getTargetWeekDay().contains(dayOfWeek)) {
            log.info("推送检查：不是周一或者周六，跳过执行 timezone:[{}] time:[{}]", timezone, currentTime);
            return;
        }
        List<Member> members = listMemberByTimeZone(timezone);
        if (CollectionUtils.isEmpty(members)) {
            log.info("该时区下没有用户，跳过执行 timezone:[{}] time:[{}]", timezone, currentTime);
            return;
        }
        String message = "I know you are hungry. It's time to eat and snap.";
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            message = "Snap. Earn. Repeat! Secure your position and earn more!";
        }
        for (Member member : members) {
            awsSnsMobilePushService.publish(member.getId(), message);
        }
    }

    private List<Member> listMemberByTimeZone(Integer timezone) {
        return memberMapper.selectList(Wrappers.lambdaQuery(Member.class).eq(Member::getTimezone, timezone));
    }

}
