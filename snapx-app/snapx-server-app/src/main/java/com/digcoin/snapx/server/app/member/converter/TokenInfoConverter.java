package com.digcoin.snapx.server.app.member.converter;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.digcoin.snapx.server.app.member.dto.TokenInfoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenInfoConverter {

    TokenInfoDTO intoDTO(SaTokenInfo saTokenInfo);

}
