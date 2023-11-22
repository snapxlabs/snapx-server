package com.digcoin.snapx.server.app.member.converter;

import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.system.entity.AdminUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrentUserConverter {

    @Mapping(source = "account", target = "username")
    CurrentUser fromMember(Member member);

}
