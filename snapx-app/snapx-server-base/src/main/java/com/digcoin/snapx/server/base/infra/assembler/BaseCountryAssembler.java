package com.digcoin.snapx.server.base.infra.assembler;

import com.digcoin.snapx.core.dto.DTOAssembler;
import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.server.base.infra.dto.CountryDTO;
import org.mapstruct.Mapper;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/5 15:59
 * @description
 */
@Mapper(componentModel = "spring")
public interface BaseCountryAssembler extends DTOAssembler<CountryDTO, GeoCountry> {
}
