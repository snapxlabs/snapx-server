package com.digcoin.snapx.server.app.member.converter;

import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.server.app.member.dto.MemberProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberProfileConverter {

    @Mapping(source = "memberId", target = "id")
    Member fromDTO(MemberProfileDTO memberProfileDTO);

}
