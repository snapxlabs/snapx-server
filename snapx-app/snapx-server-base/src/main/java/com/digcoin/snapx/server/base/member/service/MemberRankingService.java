package com.digcoin.snapx.server.base.member.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.member.bo.MemberQuery;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.service.MemberInvitationService;
import com.digcoin.snapx.domain.member.service.MemberPointsHistoryRankingService;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewService;
import com.digcoin.snapx.domain.trade.bo.BaseAccountRecentDayRanking;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.server.base.member.dto.MemberPointsRankingDTO;
import com.digcoin.snapx.server.base.member.dto.MemberPointsRankingSumDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRankingService {

    private final MemberPointsHistoryRankingService memberPointsHistoryRankingService;
    private final MemberService memberService;
    private final RestaurantReviewService restaurantReviewService;
    private final PointsAccountsService pointsAccountsService;
    private final MemberInvitationService memberInvitationService;

    public void rebuild() {
        MemberQuery query = new MemberQuery();
        query.setPage(1);
        query.setPageSize(100000);
        PageResult<Member> pageResult = memberService.pageMember(query);
        List<Member> data = pageResult.getData();
        Set<Long> memberIds = data.stream().map(Member::getId).collect(Collectors.toSet());
        Map<Long, Long> balanceMap = pointsAccountsService.getRawBalanceMap(BaseAccountsCategory.AVAILABLE, memberIds);
        memberPointsHistoryRankingService.updateRanking(redisOperations -> {
            for (Long memberId : memberIds) {
                Long balance = balanceMap.getOrDefault(memberId, 0L);
                memberPointsHistoryRankingService.updateRanking(memberId, LocalDateTime.now(), balance, true, redisOperations);
            }
        });
    }

    public MemberPointsRankingSumDTO getMemberPointsRankingSum() {
        BigDecimal totalPointsCount = pointsAccountsService.getTotalAmountSum();
        Long totalPhotosCount = restaurantReviewService.getTotalPhotosCount();
        Long totalSteakCount = memberService.getTotalSteakCount();
        MemberPointsRankingSumDTO result = new MemberPointsRankingSumDTO();
        result.setTotalPointsCount(totalPointsCount);
        result.setTotalPhotoCont(totalPhotosCount);
        result.setTotalSteakCount(totalSteakCount);
        return result;
    }

    public PageResult<MemberPointsRankingDTO> pageMemberPointsRanking(Integer page, Integer pageSize) {
        List<MemberPointsRankingDTO> records = new ArrayList<>();
        List<Long> memberIdList = memberPointsHistoryRankingService.listRanking((Long.valueOf(page) - 1L) * Long.valueOf(pageSize), Long.valueOf(pageSize));
        if (CollectionUtils.isEmpty(memberIdList)) {
            return PageResult.emptyResult();
        }

        Map<Long, Long> rankingMap = memberPointsHistoryRankingService.getRankingMap(memberIdList);
        Map<Long, Double> scoreMap = memberPointsHistoryRankingService.getMemberScore(memberIdList);
        Long totalCount = memberPointsHistoryRankingService.countRanking();
        Map<Long, Member> memberMap = memberService.mappingMember(memberIdList);
        Map<Long, Long> photoNumMap = restaurantReviewService.mappingMemberPhotosNum(memberIdList);
        Map<Long, BigDecimal> recentDaysEarnAmountMap = pointsAccountsService.getRecentDaysEarnAmountSum(memberIdList, 7);
        Map<Long, Long> inviteeCountMap = memberInvitationService.getInviteeCountMap(memberIdList);
        for (Long memberId : memberIdList) {
            Optional<Member> member = Optional.ofNullable(memberMap.get(memberId));
            Optional<Long> rank = Optional.ofNullable(rankingMap.get(memberId));
            Optional<Double> score = Optional.ofNullable(scoreMap.get(memberId));
            MemberPointsRankingDTO result = new MemberPointsRankingDTO();
            records.add(result);
            result.setMemberId(memberId);
            result.setRank(rank.orElse(null));
            result.setName(member.map(Member::getNickname).orElse(null));
            result.setSnap(photoNumMap.getOrDefault(memberId, 0L).intValue());
            result.setSteak(member.map(this::getSteak).orElse(0));
            result.setLast7Days(recentDaysEarnAmountMap.getOrDefault(memberId, BigDecimal.ZERO));
            result.setTotalPoints(score
                    .map(BigDecimal::new)
                    .map(x -> x.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))
                    .orElse(null));
            result.setInviteeCount(0L);
            Optional.ofNullable(inviteeCountMap.get(memberId)).ifPresent(result::setInviteeCount);
        }

        PageResult<MemberPointsRankingDTO> pageResult = PageResult.emptyResult();
        pageResult.setData(records);
        pageResult.setCurrPage(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalCount(totalCount.intValue());
        pageResult.setTotalPage(pageResult.getTotalCount() / pageResult.getPageSize());
        if (pageResult.getTotalPage() % pageSize != 0) {
            pageResult.setTotalPage(pageResult.getTotalPage() + 1);
        }

        return pageResult;
    }

    public PageResult<MemberPointsRankingDTO> pageMemberPointsRankingV2(Integer page, Integer pageSize) {
        List<MemberPointsRankingDTO> records = new ArrayList<>();
        List<BaseAccountRecentDayRanking> baseAccountRecentDayRankings = pointsAccountsService.pageRecentDayRanking((Long.valueOf(page) - 1L) * Long.valueOf(pageSize), Long.valueOf(pageSize), 60);
        List<Long> memberIdList = baseAccountRecentDayRankings.stream().map(BaseAccountRecentDayRanking::getMemberId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(memberIdList)) {
            return PageResult.emptyResult();
        }

        Map<Long, Long> rankingMap = baseAccountRecentDayRankings.stream().collect(Collectors.toMap(BaseAccountRecentDayRanking::getMemberId, BaseAccountRecentDayRanking::getRank));
        Map<Long, Double> scoreMap = memberPointsHistoryRankingService.getMemberScore(memberIdList);
        Long totalCount = memberPointsHistoryRankingService.countRanking();
        Map<Long, Member> memberMap = memberService.mappingMember(memberIdList);
        Map<Long, Long> photoNumMap = restaurantReviewService.mappingMemberPhotosNum(memberIdList);
        Map<Long, BigDecimal> recentDaysEarnAmountMap = baseAccountRecentDayRankings.stream().collect(Collectors.toMap(BaseAccountRecentDayRanking::getMemberId, BaseAccountRecentDayRanking::getAmountBigDecimal));
        Map<Long, Long> inviteeCountMap = memberInvitationService.getInviteeCountMap(memberIdList);
        for (Long memberId : memberIdList) {
            Optional<Member> member = Optional.ofNullable(memberMap.get(memberId));
            Optional<Long> rank = Optional.ofNullable(rankingMap.get(memberId));
            Optional<Double> score = Optional.ofNullable(scoreMap.get(memberId));
            MemberPointsRankingDTO result = new MemberPointsRankingDTO();
            records.add(result);
            result.setMemberId(memberId);
            result.setRank(rank.orElse(null));
            result.setName(member.map(Member::getNickname).orElse(null));
            result.setSnap(photoNumMap.getOrDefault(memberId, 0L).intValue());
            result.setSteak(member.map(this::getSteak).orElse(0));
            result.setLast7Days(recentDaysEarnAmountMap.getOrDefault(memberId, BigDecimal.ZERO));
            result.setTotalPoints(score
                    .map(BigDecimal::new)
                    .map(x -> x.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))
                    .orElse(null));
            result.setInviteeCount(0L);
            Optional.ofNullable(inviteeCountMap.get(memberId)).ifPresent(result::setInviteeCount);
        }

        PageResult<MemberPointsRankingDTO> pageResult = PageResult.emptyResult();
        pageResult.setData(records);
        pageResult.setCurrPage(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalCount(totalCount.intValue());
        pageResult.setTotalPage(pageResult.getTotalCount() / pageResult.getPageSize());
        if (pageResult.getTotalPage() % pageSize != 0) {
            pageResult.setTotalPage(pageResult.getTotalPage() + 1);
        }

        return pageResult;
    }

    private Integer getSteak(Member member) {
        if (Objects.isNull(member.getSteakExpireAt())) {
            return 0;
        }
        if (LocalDateTime.now().isAfter(member.getSteakExpireAt())) {
            return 0;
        }
        return member.getSteak();
    }

}
