package com.digcoin.snapx.server.base.member.service;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.member.bo.InviterCommissionQuery;
import com.digcoin.snapx.domain.member.entity.InviterCommission;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.entity.MemberInvitation;
import com.digcoin.snapx.domain.member.service.InviterCommissionService;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.trade.bo.BaseAccountsDetailsFilter;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.domain.trade.service.UsdcAccountsService;
import com.digcoin.snapx.server.base.member.converter.InviterCommissionConverter;
import com.digcoin.snapx.server.base.member.dto.InviterCommissionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviterCommissionAppService {

    private final InviterCommissionService inviterCommissionService;
    private final PointsAccountsService pointsAccountsService;
    private final UsdcAccountsService usdcAccountsService;
    private final IdentifierGenerator identifierGenerator;
    private final MemberService memberService;
    private final BaseAccountsDetailsService baseAccountsDetailsService;
    private final InviterCommissionConverter inviterCommissionConverter;

    @Transactional(rollbackFor = Exception.class)
    public Long createPointsInviterCommission(MemberInvitation invitation, BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        InviterCommission commission = new InviterCommission();
        Long commissionId = identifierGenerator.nextId(commission).longValue();

        String shareRatio = invitation.getPointsSharingRatio();
        Long commissionAmountRaw = calculate(accountsDetails.getAmount(), shareRatio);
        BigDecimal commissionAmount = pointsAccountsService.translate(commissionAmountRaw);

        Long detailsId = pointsAccountsService.increasePointsByInviterCommission(invitation.getInviterMemberId(), commissionId, commissionAmount);

        commission.setId(commissionId);
        commission.setInviterMemberId(invitation.getInviterMemberId());
        commission.setInviteeMemberId(invitation.getInviteeMemberId());
        commission.setInviteeIncomeAmount(accountsDetails.getAmount());
        commission.setInviterCommissionAmount(commissionAmountRaw);
        commission.setAccountType(String.valueOf(accounts.getAccountsType()));
        commission.setSharingRatio(shareRatio);
        commission.setInviteeAccountDetailsId(accountsDetails.getId());
        commission.setInviterAccountDetailsId(detailsId);

        inviterCommissionService.createInviterCommission(commission);

        return commission.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createUsdcInviterCommission(MemberInvitation invitation, BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        InviterCommission commission = new InviterCommission();
        Long commissionId = identifierGenerator.nextId(commission).longValue();

        String shareRatio = invitation.getUsdcSharingRatio();
        Long commissionAmountRaw = calculate(accountsDetails.getAmount(), shareRatio);
        BigDecimal commissionAmount = usdcAccountsService.translate(commissionAmountRaw);

        Long detailsId = usdcAccountsService.increasePointsByInviterCommission(invitation.getInviterMemberId(), commissionId, commissionAmount);

        commission.setId(commissionId);
        commission.setInviterMemberId(invitation.getInviterMemberId());
        commission.setInviteeMemberId(invitation.getInviteeMemberId());
        commission.setInviteeIncomeAmount(accountsDetails.getAmount());
        commission.setInviterCommissionAmount(commissionAmountRaw);
        commission.setAccountType(String.valueOf(accounts.getAccountsType()));
        commission.setSharingRatio(shareRatio);
        commission.setInviteeAccountDetailsId(accountsDetails.getId());
        commission.setInviterAccountDetailsId(detailsId);

        inviterCommissionService.createInviterCommission(commission);

        return commission.getId();
    }

    public PageResult<InviterCommissionDTO> pageInviterCommission(InviterCommissionQuery query) {
        PageResult<InviterCommission> pageResult = inviterCommissionService.pageInviterCommission(query);

        Set<Long> inviterMemberIdSet = pageResult.getData().stream().map(InviterCommission::getInviterMemberId).collect(Collectors.toSet());
        Set<Long> inviteeMemberIdSet = pageResult.getData().stream().map(InviterCommission::getInviteeMemberId).collect(Collectors.toSet());
        Set<Long> allMemberIds = Stream.concat(inviterMemberIdSet.stream(), inviteeMemberIdSet.stream()).collect(Collectors.toSet());
        Map<Long, Member> memberMap = memberService.mappingMember(allMemberIds);

        List<BaseAccountsDetails> baseAccountsDetailsList = baseAccountsDetailsService.listBaseAccountsDetails(
                BaseAccountsDetailsFilter.builder().build());
        Map<Long, BaseAccountsDetails> accountsDetailsMap = baseAccountsDetailsList.stream()
                .collect(Collectors.toMap(BaseAccountsDetails::getId, Function.identity()));

        Function<InviterCommission, InviterCommissionDTO> converter = entity -> {
            InviterCommissionDTO dto = new InviterCommissionDTO();
            dto.setId(entity.getId());
            dto.setSharingRatio(entity.getSharingRatio());
            dto.setInviteeIncomeAmount(translate(query.getAccountType(), entity.getInviteeIncomeAmount()));
            dto.setInviterCommissionAmount(translate(query.getAccountType(), entity.getInviterCommissionAmount()));
            dto.setInviter(inviterCommissionConverter.intoDTO(memberMap.getOrDefault(entity.getInviterMemberId(), new Member())));
            dto.setInvitee(inviterCommissionConverter.intoDTO(memberMap.getOrDefault(entity.getInviteeMemberId(), new Member())));
            dto.setDetails(inviterCommissionConverter.intoDTO(accountsDetailsMap.getOrDefault(entity.getInviteeAccountDetailsId(), new BaseAccountsDetails())));
            return dto;
        };
        return PageResult.fromPageResult(pageResult, converter);
    }

    private Long calculate(Long amount, String shareRatio) {
        if (Objects.isNull(amount) || StringUtils.isBlank(shareRatio)) {
            return 0L;
        }
        BigDecimal decimal = new BigDecimal(amount);
        BigDecimal ratio = new BigDecimal(shareRatio);
        return ratio.divide(new BigDecimal(100)).multiply(decimal).longValue();
    }

    private BigDecimal translate(String accountType, Long amount) {
        if (BaseAccountsType.POINTS.name().equalsIgnoreCase(accountType)) {
            return pointsAccountsService.translate(amount);
        } else if (BaseAccountsType.USDC.name().equalsIgnoreCase(accountType)) {
            return usdcAccountsService.translate(amount);
        } else {
            return pointsAccountsService.translate(amount);
        }
    }

}
