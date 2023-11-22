package com.digcoin.snapx.server.admin.system.converter;

import com.digcoin.snapx.server.admin.system.dto.TokenInfoDTO;
import cn.dev33.satoken.stp.SaTokenInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenInfoConverter {

    TokenInfoDTO intoDTO(SaTokenInfo saTokenInfo);

}
