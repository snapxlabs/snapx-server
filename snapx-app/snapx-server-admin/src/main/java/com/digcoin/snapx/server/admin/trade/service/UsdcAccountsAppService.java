package com.digcoin.snapx.server.admin.trade.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.member.bo.MemberQuery;
import com.digcoin.snapx.domain.trade.bo.BaseAccountsDetailsQuery;
import com.digcoin.snapx.domain.trade.entity.BaseAccounts;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.enums.BaseAccountsCategory;
import com.digcoin.snapx.domain.trade.service.BaseAccountsDetailsService;
import com.digcoin.snapx.domain.trade.service.UsdcAccountsService;
import com.digcoin.snapx.server.admin.member.dto.MemberDTO;
import com.digcoin.snapx.server.admin.member.service.MemberAppService;
import com.digcoin.snapx.server.admin.trade.dto.BaseAccountsDTO;
import com.digcoin.snapx.server.admin.trade.dto.BaseAccountsModifyDTO;
import com.digcoin.snapx.server.base.trade.converter.UsdcAccountsDetailsConverter;
import com.digcoin.snapx.server.base.trade.dto.UsdcAccountsDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsdcAccountsAppService {

    private final UsdcAccountsService usdcAccountsService;
    private final MemberAppService memberAppService;
    private final BaseAccountsDetailsService walletAccountsDetailsService;
    private final UsdcAccountsDetailsConverter usdcAccountsDetailsConverter;

    public PageResult<BaseAccountsDTO> pageAccounts(MemberQuery query) {
        PageResult<MemberDTO> memberPage = memberAppService.pageMember(query);
        if (CollectionUtils.isEmpty(memberPage.getData())) {
            return PageResult.fromPageResult(memberPage, x -> new BaseAccountsDTO());
        }
        Set<Long> memberIds = memberPage.getData().stream().map(MemberDTO::getId).collect(Collectors.toSet());
        Map<Long, BaseAccounts> accountsMap = usdcAccountsService.getAccountsMap(memberIds);
        Function<MemberDTO, BaseAccountsDTO> converter = memberDTO -> {
            BaseAccountsDTO dto = new BaseAccountsDTO();
            dto.setMember(memberDTO);
            BaseAccountsDTO.Accounts accounts = new BaseAccountsDTO.Accounts();
            dto.setAccounts(accounts);

            Optional.ofNullable(accountsMap.get(memberDTO.getId()))
                    .ifPresent(x -> accounts.setBalance(usdcAccountsService.translate(x.getBalance())));

            return dto;
        };
        return PageResult.fromPageResult(memberPage, converter);
    }

    public PageResult<UsdcAccountsDetailsDTO> pageAccountsDetails(Long memberId, BaseAccountsDetailsQuery query) {
        BaseAccounts availableAccounts = usdcAccountsService.findAccounts(memberId, BaseAccountsCategory.AVAILABLE);
        query.setWalletAccountsId(availableAccounts.getId());
        query.setCustomerVisible(true);
        PageResult<BaseAccountsDetails> pageResult = walletAccountsDetailsService.pageBaseAccountsDetails(query);
        return PageResult.fromPageResult(pageResult, usdcAccountsDetailsConverter::intoDTO);
    }

    public void updateAccounts(BaseAccountsModifyDTO payload) {
        if (payload.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            usdcAccountsService.reducePointsByAdminModify(payload.getMemberId(), payload.getAmount().abs(), payload.getReason());
        } else if (payload.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            usdcAccountsService.increasePointsByAdminModify(payload.getMemberId(), payload.getAmount(), payload.getReason());
        } else {
            // 修改数额为0时，不做任何操作
        }
    }

}
