package com.digcoin.snapx.server.app.restaurant.service;

import com.digcoin.snapx.domain.infra.bo.LocationBO;
import com.digcoin.snapx.domain.infra.service.LocationService;
import com.digcoin.snapx.domain.restaurant.bo.LatLngDTO;
import com.digcoin.snapx.server.app.infra.converter.LocationConverter;
import com.digcoin.snapx.server.app.infra.dto.LocationListLocalitiesQryDTO;
import com.digcoin.snapx.server.app.infra.dto.LocationLocateQryDTO;
import com.digcoin.snapx.server.app.infra.vo.LocationVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/16 10:37
 * @description
 */
@Slf4j
@AllArgsConstructor
@Service
public class AppLocationService {

    private final LocationService locationService;
    private final LocationConverter locationConverter;

    @Transactional(rollbackFor = Exception.class)
    public LocationVO locate(LocationLocateQryDTO dto) {
        LatLngDTO latLng = dto.getLatLng();
        LocationBO location = locationService.getSystemLocationByLatLng(latLng.getLat(), latLng.getLng());
        return locationConverter.toVO(location);
    }

    public List<LocationVO> listLocalities(LocationListLocalitiesQryDTO dto) {
        return locationConverter.toVO(locationService.listLocalities(dto.getKeyword(), false, null, null));
    }

    public List<LocationVO> listRecLocalities(LocationListLocalitiesQryDTO dto) {
        return locationConverter.toVO(locationService.listLocalities(dto.getKeyword(), true, dto.getLat(), dto.getLng()));
    }
}
