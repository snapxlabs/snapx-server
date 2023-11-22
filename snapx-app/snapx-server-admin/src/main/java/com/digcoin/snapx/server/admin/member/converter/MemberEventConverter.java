package com.digcoin.snapx.server.admin.member.converter;

import com.digcoin.snapx.domain.member.entity.MemberEventAggregation;
import com.digcoin.snapx.server.admin.member.dto.MemberEventDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberEventConverter {

    MemberEventDTO intoDTO(MemberEventAggregation entity);

}
