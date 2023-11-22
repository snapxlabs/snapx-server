package com.digcoin.snapx.server.base.trade.converter;

import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.service.UsdcAccountsService;
import com.digcoin.snapx.server.base.trade.dto.UsdcAccountsDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UsdcAccountsDetailsConverter {

    @Autowired
    protected UsdcAccountsService usdcAccountsService;

    @Mapping(target = "amount", expression = "java(usdcAccountsService.translate(entity.getAmount()))")
    @Mapping(target = "balanceBefore", expression = "java(usdcAccountsService.translate(entity.getBalanceBefore()))")
    @Mapping(target = "balanceAfter", expression = "java(usdcAccountsService.translate(entity.getBalanceAfter()))")
    public abstract UsdcAccountsDetailsDTO intoDTO(BaseAccountsDetails entity);

}
