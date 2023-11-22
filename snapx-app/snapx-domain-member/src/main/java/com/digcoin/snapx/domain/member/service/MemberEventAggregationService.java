package com.digcoin.snapx.domain.member.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.member.bo.MemberEventQuery;
import com.digcoin.snapx.domain.member.constant.MemberEventConst;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.entity.MemberEventAggregation;
import com.digcoin.snapx.domain.member.mapper.MemberEventAggregationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventAggregationService {

    private final MemberEventAggregationMapper memberEventAggregationMapper;
    private final StringRedisTemplate redisTemplate;

    public void addMemberSignUpCount(LocalDateTime time) {
        LocalDateTime aggregationTime = LocalDateTime.of(time.toLocalDate(), LocalTime.of(time.getHour(), time.getMinute()));
        MemberEventAggregation memberEventAggregation = memberEventAggregationMapper.selectOne(Wrappers.lambdaQuery(MemberEventAggregation.class)
                .eq(MemberEventAggregation::getEvent, MemberEventConst.SIGN_UP)
                .eq(MemberEventAggregation::getTime, aggregationTime));
        if (Objects.isNull(memberEventAggregation)) {
            MemberEventAggregation entity = new MemberEventAggregation();
            entity.setEvent(MemberEventConst.SIGN_UP);
            entity.setTime(aggregationTime);
            entity.setCount(1L);
            memberEventAggregationMapper.insert(entity);
        } else {
            MemberEventAggregation example = new MemberEventAggregation();
            example.setId(memberEventAggregation.getId());
            example.setCount(memberEventAggregation.getCount() + 1L);
            memberEventAggregationMapper.updateById(example);
        }
    }

    public void addMemberVisitCount(Member member, LocalDateTime time) {
        LocalDateTime aggregationTime = LocalDateTime.of(time.toLocalDate(), LocalTime.of(time.getHour(), time.getMinute()));
        MemberEventAggregation memberEventAggregation = memberEventAggregationMapper.selectOne(Wrappers.lambdaQuery(MemberEventAggregation.class)
                .eq(MemberEventAggregation::getEvent, MemberEventConst.MEMBER_VISIT)
                .eq(MemberEventAggregation::getTime, aggregationTime));
        if (Objects.isNull(memberEventAggregation)) {
            MemberEventAggregation entity = new MemberEventAggregation();
            entity.setEvent(MemberEventConst.MEMBER_VISIT);
            entity.setTime(aggregationTime);
            entity.setCount(1L);
            memberEventAggregationMapper.insert(entity);
        } else {
            MemberEventAggregation example = new MemberEventAggregation();
            example.setId(memberEventAggregation.getId());
            example.setCount(memberEventAggregation.getCount() + 1L);
            memberEventAggregationMapper.updateById(example);
        }
    }

    public void addMemberAccessCount(Member member, LocalDateTime time) {
        if (isMemberAccessedToday(member)) {
            return;
        }
        LocalDateTime aggregationTime = LocalDateTime.of(time.toLocalDate(), LocalTime.of(time.getHour(), time.getMinute()));
        MemberEventAggregation memberEventAggregation = memberEventAggregationMapper.selectOne(Wrappers.lambdaQuery(MemberEventAggregation.class)
                .eq(MemberEventAggregation::getEvent, MemberEventConst.MEMBER_ACCESS)
                .eq(MemberEventAggregation::getTime, aggregationTime));
        if (Objects.isNull(memberEventAggregation)) {
            MemberEventAggregation entity = new MemberEventAggregation();
            entity.setEvent(MemberEventConst.MEMBER_ACCESS);
            entity.setTime(aggregationTime);
            entity.setCount(1L);
            memberEventAggregationMapper.insert(entity);
        } else {
            MemberEventAggregation example = new MemberEventAggregation();
            example.setId(memberEventAggregation.getId());
            example.setCount(memberEventAggregation.getCount() + 1L);
            memberEventAggregationMapper.updateById(example);
        }
    }

    public void addMemberMonthAccessCount(Member member, LocalDateTime time) {
        if (isMemberAccessedMonth(member)) {
            return;
        }
        LocalDateTime aggregationTime = time.toLocalDate().atStartOfDay();
        MemberEventAggregation memberEventAggregation = memberEventAggregationMapper.selectOne(Wrappers.lambdaQuery(MemberEventAggregation.class)
                .eq(MemberEventAggregation::getEvent, MemberEventConst.MEMBER_MONTH_ACCESS)
                .eq(MemberEventAggregation::getTime, aggregationTime));
        if (Objects.isNull(memberEventAggregation)) {
            MemberEventAggregation entity = new MemberEventAggregation();
            entity.setEvent(MemberEventConst.MEMBER_MONTH_ACCESS);
            entity.setTime(aggregationTime);
            entity.setCount(1L);
            memberEventAggregationMapper.insert(entity);
        } else {
            MemberEventAggregation example = new MemberEventAggregation();
            example.setId(memberEventAggregation.getId());
            example.setCount(memberEventAggregation.getCount() + 1L);
            memberEventAggregationMapper.updateById(example);
        }
    }

    private boolean isMemberAccessedToday(Member member) {
        String key = "SNAPX:FILETER:MEMBER:ACCESS-EVENT";
        BoundSetOperations<String, String> operations = redisTemplate.boundSetOps(key);
        boolean hasKey = Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
        if (!hasKey) {
            operations.add("0");
            Date date = DateUtils.addDays(new Date(), 1);
            DateUtils.setHours(date, 0);
            DateUtils.setMinutes(date, 0);
            DateUtils.setSeconds(date, 0);
            DateUtils.setMilliseconds(date, 0);
            redisTemplate.expireAt(key, date);
        }
        String memberId = String.valueOf(member.getId());
        boolean isExist = Optional.ofNullable(operations.isMember(memberId)).orElse(false);
        if (!isExist) {
            operations.add(memberId);
        }
        return isExist;
    }

    private boolean isMemberAccessedMonth(Member member) {
        String key = "SNAPX:FILETER:MEMBER:MONTH-ACCESS-EVENT";
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        key = String.join(":", key, currentMonth.format(pattern));
        BoundSetOperations<String, String> operations = redisTemplate.boundSetOps(key);
        boolean hasKey = Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
        if (!hasKey) {
            operations.add("0");
            Date date = DateUtils.addMonths(new Date(), 1);
            DateUtils.setDays(date, 1);
            DateUtils.setHours(date, 0);
            DateUtils.setMinutes(date, 0);
            DateUtils.setSeconds(date, 0);
            DateUtils.setMilliseconds(date, 0);
            redisTemplate.expireAt(key, date);
        }
        String memberId = String.valueOf(member.getId());
        boolean isExist = Optional.ofNullable(operations.isMember(memberId)).orElse(false);
        if (!isExist) {
            operations.add(memberId);
        }
        return isExist;
    }

    public Long getEventSumDaily(String event, LocalDateTime currentTime) {
        LocalDateTime startTime = currentTime.toLocalDate().atStartOfDay();
        LocalDateTime endTime = currentTime.toLocalDate().plusDays(1).atStartOfDay();
        return memberEventAggregationMapper.getEventSum(event, startTime, endTime);
    }

    public Long getEventSumRecentDaily(String event, LocalDateTime currentTime) {
        LocalDateTime startTime = currentTime.minusHours(24);
        LocalDateTime endTime = currentTime.plusSeconds(1);
        return memberEventAggregationMapper.getEventSum(event, startTime, endTime);
    }

    public Long getEventSumMonth(String event, LocalDateTime currentTime) {
        LocalDateTime startTime = currentTime.toLocalDate().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endTime = startTime.plusMonths(1);
        return memberEventAggregationMapper.getEventSum(event, startTime, endTime);
    }

    public Long getEventSum(String event) {
        return memberEventAggregationMapper.getEventSum(event, null, null);
    }

    public Long getEventSumRecentMonth(String event, LocalDateTime currentTime) {
        LocalDateTime startTime = currentTime.minusDays(30);
        LocalDateTime endTime = currentTime.plusSeconds(1);
        return memberEventAggregationMapper.getEventSum(event, startTime, endTime);
    }

    public PageResult<MemberEventAggregation> pageEventPerMonth(MemberEventQuery query) {
        return PageResult.fromPage(memberEventAggregationMapper.pageEventPerMonth(
                        PageHelper.getPage(query),
                        query.getEvent(),
                        query.getStartTime(),
                        query.getEndTime()),
                Function.identity());
    }

    public PageResult<MemberEventAggregation> pageEventPerDay(MemberEventQuery query) {
        return PageResult.fromPage(memberEventAggregationMapper.pageEventPerDay(
                PageHelper.getPage(query),
                query.getEvent(),
                query.getStartTime(),
                query.getEndTime()),
                Function.identity());
    }

    public PageResult<MemberEventAggregation> pageEventPerHour(MemberEventQuery query) {
        return PageResult.fromPage(memberEventAggregationMapper.pageEventPerHour(
                PageHelper.getPage(query),
                query.getEvent(),
                query.getStartTime(),
                query.getEndTime()),
                Function.identity());
    }

}
