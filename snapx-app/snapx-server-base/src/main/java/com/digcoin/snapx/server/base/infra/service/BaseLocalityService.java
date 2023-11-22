package com.digcoin.snapx.server.base.infra.service;

import com.digcoin.snapx.core.common.util.CustomCollectionUtil;
import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.domain.infra.entity.GeoLocality;
import com.digcoin.snapx.domain.infra.service.GeoCountryService;
import com.digcoin.snapx.domain.infra.service.GeoLocalityService;
import com.digcoin.snapx.domain.restaurant.service.OrderService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantService;
import com.digcoin.snapx.server.base.infra.assembler.BaseCountryAssembler;
import com.digcoin.snapx.server.base.infra.assembler.BaseLocalityAssembler;
import com.digcoin.snapx.server.base.infra.dto.LocalityDTO;
import com.digcoin.snapx.server.base.infra.dto.MultilevelLocalityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/5 15:52
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BaseLocalityService {

    private final GeoCountryService geoCountryService;
    private final BaseCountryAssembler baseCountryAssembler;

    private final GeoLocalityService geoLocalityService;
    private final BaseLocalityAssembler baseLocalityAssembler;

    private final RestaurantService restaurantService;
    private final RestaurantReviewService restaurantReviewService;
    private final OrderService orderService;

    public List<LocalityDTO> listLocalities() {

        List<GeoCountry> countries = geoCountryService.listCountries();
        Map<Long, GeoCountry> countryMap = CustomCollectionUtil.mapping(countries, GeoCountry::getId);

        List<GeoLocality> localities = geoLocalityService.listLocalities();
        List<LocalityDTO> dtoList = localities.stream().map(locality -> {
            LocalityDTO dto = baseLocalityAssembler.toDTO(locality);

            // 国家
            GeoCountry country = countryMap.get(locality.getCountryId());
            if (Objects.nonNull(country)) {
                dto.setCountry(baseCountryAssembler.toDTO(country));
            }

            return dto;
        }).collect(Collectors.toList());
        return dtoList;
    }

    public List<MultilevelLocalityDTO> listMultilevelLocalities() {
        // 国家
        List<GeoCountry> countries = geoCountryService.listCountries();
        List<MultilevelLocalityDTO> dtoList = countries.stream().map(country -> {
            MultilevelLocalityDTO dto = new MultilevelLocalityDTO();
            BeanUtils.copyProperties(country, dto);
            dto.setType(MultilevelLocalityDTO.Type.COUNTRY);
            dto.setValue(country.getId());
            dto.setLabel(country.getLongName());
            return dto;
        }).collect(Collectors.toList());

        // 城市
        List<GeoLocality> localities = geoLocalityService.listLocalities();
        for (GeoLocality locality : localities) {
            for (MultilevelLocalityDTO multilevelLocalityDTO : dtoList) {
                if (Objects.equals(multilevelLocalityDTO.getId(), locality.getCountryId())) {
                    List<MultilevelLocalityDTO> children = Optional.ofNullable(multilevelLocalityDTO.getChildren()).orElse(new ArrayList<>());

                    MultilevelLocalityDTO dto = new MultilevelLocalityDTO();
                    BeanUtils.copyProperties(locality, dto);
                    dto.setType(MultilevelLocalityDTO.Type.LOCALITY);
                    dto.setValue(locality.getId());
                    dto.setLabel(locality.getLongName());
                    children.add(dto);
                    multilevelLocalityDTO.setChildren(children);
                }
            }
        }
        return dtoList;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean mergeLocalities(Long sourceLocalityId, Long targetLocalityId) {
        if (!geoLocalityService.mergeLocalities(sourceLocalityId, targetLocalityId)) {
            return false;
        }

        restaurantService.updateNewLocalityId(sourceLocalityId, targetLocalityId);
        restaurantReviewService.updateNewLocalityId(sourceLocalityId, targetLocalityId);
        orderService.updateNewLocalityId(sourceLocalityId, targetLocalityId);

        return true;
    }

}
