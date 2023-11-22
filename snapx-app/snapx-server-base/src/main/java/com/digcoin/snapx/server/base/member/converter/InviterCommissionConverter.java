package com.digcoin.snapx.server.base.member.converter;

import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.trade.entity.BaseAccountsDetails;
import com.digcoin.snapx.server.base.member.dto.InviterCommissionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InviterCommissionConverter {

    InviterCommissionDTO.MemberDTO intoDTO(Member entity);

    InviterCommissionDTO.BaseAccountsDetailsDTO intoDTO(BaseAccountsDetails entity);

}
