package com.digcoin.snapx.server.base.member.service;

import com.digcoin.snapx.domain.member.constant.MemberIdentity;
import com.digcoin.snapx.domain.member.entity.MemberInvitation;
import com.digcoin.snapx.domain.member.service.MemberInvitationService;
import com.digcoin.snapx.domain.trade.constant.BaseAccountsFinancialSubject;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsType;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class InviterCommissionUpdateService extends BaseAccountsEventHandleService {

    private final InviterCommissionAppService inviterCommissionAppService;
    private final MemberInvitationService memberInvitationService;

    public InviterCommissionUpdateService(BaseAccountsDetailsService baseAccountsDetailsService,
                                          PointsAccountsService pointsAccountsService,
                                          InviterCommissionAppService inviterCommissionAppService,
                                          MemberInvitationService memberInvitationService) {
        super(baseAccountsDetailsService, pointsAccountsService);
        this.inviterCommissionAppService = inviterCommissionAppService;
        this.memberInvitationService = memberInvitationService;
    }

    @Override
    protected Boolean willHandle(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        // 跳过分佣收入，不进行分佣处理。只有上下两级的分佣体系
        if (BaseAccountsFinancialSubject.INVITER_COMMISSION.equals(accountsDetails.getSubject())) {
            return false;
        }
        return true;
    }

    @Override
    protected void doHandleTransferInEvent(BaseAccounts accounts, BaseAccountsDetails accountsDetails) {
        MemberInvitation invitation = memberInvitationService.findMemberInvitationByInvitee(accounts.getMemberId());
        if (Objects.isNull(invitation) || !MemberIdentity.AGENT.equals(invitation.getIdentity())) {
            return;
        }
        if (BaseAccountsType.POINTS.equals(accounts.getAccountsType())) {
            inviterCommissionAppService.createPointsInviterCommission(invitation, accounts, accountsDetails);
            return;
        }
        if (BaseAccountsType.USDC.equals(accounts.getAccountsType())) {
            inviterCommissionAppService.createUsdcInviterCommission(invitation, accounts, accountsDetails);
            return;
        }
    }
}
