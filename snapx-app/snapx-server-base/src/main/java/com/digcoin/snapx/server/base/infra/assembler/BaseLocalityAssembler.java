package com.digcoin.snapx.server.base.infra.assembler;

import com.digcoin.snapx.core.dto.DTOAssembler;
import com.digcoin.snapx.domain.infra.entity.GeoLocality;
import com.digcoin.snapx.server.base.infra.dto.LocalityDTO;
import org.mapstruct.Mapper;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/5 15:57
 * @description
 */
@Mapper(componentModel = "spring")
public interface BaseLocalityAssembler extends DTOAssembler<LocalityDTO, GeoLocality> {

}
