package com.digcoin.snapx.server.admin.member.converter;

import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.server.admin.member.dto.MemberDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberConverter {

    MemberDTO intoDTO(Member entity);

    Member fromMember(MemberDTO dto);

}
