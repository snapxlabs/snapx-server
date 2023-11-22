package com.digcoin.snapx.server.base.system.converter;

import com.digcoin.snapx.domain.system.entity.ContentInformation;
import com.digcoin.snapx.server.base.system.dto.ContentInformationDTO;
import com.digcoin.snapx.server.base.system.dto.ContentInformationItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContentInformationConverter {

    ContentInformationDTO intoDTO(ContentInformation entity);

    ContentInformationItemDTO intoItemDTO(ContentInformation entity);

    ContentInformation fromDTO(ContentInformationDTO dto);

}
