package com.digcoin.snapx.server.app.member.converter;

import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.server.app.member.converter.decorator.MemberConverterDecorator;
import com.digcoin.snapx.server.app.member.dto.MemberDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(MemberConverterDecorator.class)
public interface MemberConverter {

    Member fromDTO(MemberDTO member);

    MemberDTO intoDTO(Member member);

}
