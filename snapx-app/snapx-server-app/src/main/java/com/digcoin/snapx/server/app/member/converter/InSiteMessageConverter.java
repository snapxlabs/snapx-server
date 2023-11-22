package com.digcoin.snapx.server.app.member.converter;

import com.digcoin.snapx.domain.infra.entity.InSiteMessage;
import com.digcoin.snapx.server.app.infra.dto.InSiteMessageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InSiteMessageConverter {

    InSiteMessageDTO intoDTO(InSiteMessage entity);

}
