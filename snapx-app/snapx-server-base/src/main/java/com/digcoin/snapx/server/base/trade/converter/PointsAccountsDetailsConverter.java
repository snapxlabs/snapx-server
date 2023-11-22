package com.digcoin.snapx.server.base.trade.converter;

import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.domain.trade.service.PointsAccountsService;
import com.digcoin.snapx.server.base.trade.dto.PointsAccountsDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PointsAccountsDetailsConverter {

    @Autowired
    protected PointsAccountsService pointsAccountsService;

    @Mapping(target = "amount", expression = "java(pointsAccountsService.translate(entity.getAmount()))")
    @Mapping(target = "balanceBefore", expression = "java(pointsAccountsService.translate(entity.getBalanceBefore()))")
    @Mapping(target = "balanceAfter", expression = "java(pointsAccountsService.translate(entity.getBalanceAfter()))")
    public abstract PointsAccountsDetailsDTO intoDTO(BaseAccountsDetails entity);

}
