package com.digcoin.snapx.server.base.infra.service;

import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.domain.infra.service.GeoCountryService;
import com.digcoin.snapx.server.base.infra.assembler.BaseCountryAssembler;
import com.digcoin.snapx.server.base.infra.dto.CountryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/5 16:00
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BaseCountryService {

    private final GeoCountryService geoCountryService;
    private final BaseCountryAssembler baseCountryAssembler;

    public List<CountryDTO> listCountries() {
        List<GeoCountry> list = geoCountryService.listCountries();
        return baseCountryAssembler.toDTO(list);
    }

}
