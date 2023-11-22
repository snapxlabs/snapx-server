package com.digcoin.snapx.domain.member.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.common.util.BitFlagUtil;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.member.bo.MemberQuery;
import com.digcoin.snapx.domain.member.constant.MemberFlag;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.error.MemberDomainError;
import com.digcoin.snapx.domain.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

    public PageResult<Member> pageMember(MemberQuery query) {
        return PageResult.fromPage(memberMapper.selectPage(PageHelper.getPage(query), Wrappers.lambdaQuery(Member.class)
                .like(StringUtils.isNotBlank(query.getAccount()), Member::getAccount, query.getAccount())
                .like(StringUtils.isNotBlank(query.getNickname()), Member::getNickname, query.getNickname())
                .eq(Objects.nonNull(query.getGender()), Member::getGender, query.getGender())
                .ge(Objects.nonNull(query.getCreateTimeStart()), Member::getCreateTime, query.getCreateTimeStart())
                .le(Objects.nonNull(query.getCreateTimeEnd()), Member::getCreateTime, query.getCreateTimeEnd())
                .in(CollectionUtils.isNotEmpty(query.getMemberIds()), Member::getId, query.getMemberIds())
                .eq(Objects.nonNull(query.getVirtualUser()), Member::getVirtualUser, query.getVirtualUser())
                .eq(Objects.nonNull(query.getFreeze()), Member::getFreeze, query.getFreeze())
                .like(StringUtils.isNotBlank(query.getPhone()), Member::getPhone, query.getPhone())
                .orderByDesc(Member::getCreateTime)
        ), Function.identity());
    }

    public Map<Long, Member> mappingMember(Collection<Long> memberIds) {
        if (CollectionUtils.isEmpty(memberIds)) {
            return Collections.emptyMap();
        }
        return listMember(memberIds).stream().collect(Collectors.toMap(Member::getId, Function.identity()));
    }

    public Map<String, Member> mappingMemberByAccount(Collection<String> accounts) {
        if (CollectionUtils.isEmpty(accounts)) {
            return Collections.emptyMap();
        }
        return listMemberByAccount(accounts).stream().collect(Collectors.toMap(Member::getAccount, Function.identity()));
    }

    public List<Member> listMember(Collection<Long> memberIds) {
        if (CollectionUtils.isEmpty(memberIds)) {
            return Collections.emptyList();
        }
        return memberMapper.selectList(Wrappers.lambdaQuery(Member.class).in(Member::getId, memberIds));
    }

    public List<Member> listAllMember() {
        return memberMapper.selectList(Wrappers.lambdaQuery(Member.class));
    }

    public List<Member> listMemberByAccount(Collection<String> accounts) {
        if (CollectionUtils.isEmpty(accounts)) {
            return Collections.emptyList();
        }
        return memberMapper.selectList(Wrappers.lambdaQuery(Member.class).in(Member::getAccount, accounts));
    }

    public Member findMember(Long id) {
        return memberMapper.selectById(id);
    }

    public Member findMemberByAccount(String account) {
        return memberMapper.selectOne(Wrappers.lambdaQuery(Member.class).eq(Member::getAccount, account));
    }

    public Member findMemberByPhone(String areaCode, String phone) {
        return memberMapper.selectOne(Wrappers.lambdaQuery(Member.class)
                .eq(Member::getPhoneAreaCode, areaCode)
                .eq(Member::getPhone, phone));
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createMember(Member member) {
        String account = member.getAccount();
        Member existedMember = findMemberByAccount(account);
        if (Objects.nonNull(existedMember)) {
            throw MemberDomainError.EMAIL_HAS_BEEN_BOUND.withDefaults();
        }

        memberMapper.insert(member);

        return member.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMember(Member member) {
        member.setAccount(null);
        memberMapper.updateById(member);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMember(Long memberId) {
        memberMapper.deleteById(memberId);
    }

    public boolean isInviteBonus(Long memberId) {
        Member member = memberMapper.selectById(memberId);
        return BitFlagUtil.isOn(member.getMemberFlag(), MemberFlag.INVITE_BONUS);
    }

    public boolean isLoginForFirstTime(Long memberId) {
        Member member = memberMapper.selectById(memberId);
        return BitFlagUtil.isOn(member.getMemberFlag(), MemberFlag.LOGIN_FOR_FIRST_TIME);
    }

    public boolean isNotifyTwoDaysLeave(Member member) {
        return BitFlagUtil.isOn(member.getMemberFlag(), MemberFlag.NOTIFY_TWO_DAYS_LEAVE);
    }

    public void updateMemberFlagOn(Long memberId, long memberFlag) {
        updateMemberFlag(memberId, memberFlag, BitFlagUtil::turnOn);
    }

    public void updateMemberFlagOff(Long memberId, long memberFlag) {
        updateMemberFlag(memberId, memberFlag, BitFlagUtil::turnOff);
    }

    private void updateMemberFlag(Long memberId, long memberFlag, BiFunction<Long, Long, Long> handler) {
        long oldStatus = memberMapper.selectById(memberId).getMemberFlag();
        while (true) {
            long newStatus = handler.apply(oldStatus, memberFlag);
            int update = memberMapper.update(new Member(), Wrappers.lambdaUpdate(Member.class)
                    .eq(Member::getId, memberId)
                    .eq(Member::getMemberFlag, oldStatus)
                    .set(Member::getMemberFlag, newStatus));
            if (update > 0) {
                break;
            }
            oldStatus = memberMapper.selectById(memberId).getMemberFlag();
        }
    }

    public void updateMemberTotalEarned(Member member, BigDecimal totalEarned) {
        if (Objects.isNull(member)) {
            return;
        }
        totalEarned = Optional.ofNullable(totalEarned).orElse(BigDecimal.ZERO);
        LambdaUpdateWrapper<Member> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Member::getId, member.getId());
        updateWrapper.set(Member::getTotalEarned, totalEarned);
        memberMapper.update(null, updateWrapper);
    }

    public void updateMemberTotalEarned(Long memberId, BigDecimal totalEarned) {
        updateMemberTotalEarned(findMember(memberId), totalEarned);
    }

    /**
     * 计算steak值
     * steak：连续拍照天数
     * steakAt：连续拍照时间段里的第一天
     * steakExpireAt：最后一次拍照的第二天结束
     * steak的计算方法：
     * 1. 取当前时间与steakAt之间间隔的天数+1，
     * 2. 如果当前时间的时分秒，小于steakAt的时分秒则再+1，因为虽然不足24小时，但是已跨越1天
     *
     * @param memberId
     */
    public void updateSteak(Long memberId) {
        try {
            Member member = memberMapper.selectById(memberId);
            if (Objects.isNull(member)) {
                return;
            }
            LocalDateTime now = LocalDateTime.now();

            // 连续拍照时间段里的第一天
            LocalDateTime steakAt = Optional.ofNullable(member.getSteakAt()).orElse(now);
            // 最后一次拍照的第二天结束
            LocalDateTime steakExpireAt = member.getSteakExpireAt();
            // 判断steak是否已经过期，过期则将拍照开始时间（steakAt）重置为当前时间
            if (Objects.nonNull(steakExpireAt) && !now.isBefore(steakExpireAt)) {
                steakAt = now;
            }
            // 计算steak值
            // 取当前时间（now）与拍照开始时间（steakAt）之间间隔的天数+1，
            // 如果不是同一天，但是当前时分秒小于steakAt时分秒，间隔的天数会少1，但是已跨天再+1补足
            Long steak = Duration.between(steakAt, now).toDays() + 1;
            if (!now.toLocalDate().isEqual(steakAt.toLocalDate())
                    && now.toLocalTime().isBefore(steakAt.toLocalTime())) {
                steak += 1;
            }

            // 过期时间更新为明天结束，日期加2天，时间抹零
            LocalDateTime nextSteakExpireAt = now.toLocalDate().plusDays(2).atStartOfDay();

            Member example = new Member();
            example.setId(memberId);
            example.setSteak(steak.intValue());
            example.setSteakAt(steakAt);
            example.setSteakExpireAt(nextSteakExpireAt);
            memberMapper.updateById(example);
        } catch (Exception e) {
            log.error("updateSteak fail memberId:[{}]", memberId, e);
        }
    }

    public Long getTotalSteakCount() {
        return memberMapper.getTotalSteakCount();
    }

    public Member findMemberOrFail(Long memberId) {
        Member member = findMember(memberId);
        if (Objects.isNull(member)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("memberId not found");
        }
        return member;
    }

}
