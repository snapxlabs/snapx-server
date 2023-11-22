package com.digcoin.snapx.server.base.infra.converter;

import com.digcoin.snapx.domain.infra.entity.ResourceFile;
import com.digcoin.snapx.server.base.infra.dto.ResourceFileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceFileConverter {

    ResourceFileDTO intoDTO(ResourceFile resourceFile);

}
