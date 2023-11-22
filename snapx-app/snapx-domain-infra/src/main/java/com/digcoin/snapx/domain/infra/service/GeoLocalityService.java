package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.domain.infra.entity.GeoLocality;
import com.digcoin.snapx.domain.infra.error.LocationError;
import com.digcoin.snapx.domain.infra.mapper.GeoLocalityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 16:07
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeoLocalityService {

    private final GeoLocalityMapper geoLocalityMapper;

    public List<GeoLocality> listLocalities() {
        return geoLocalityMapper.selectList(Wrappers.<GeoLocality>lambdaQuery()
                .orderByDesc(GeoLocality::getCreateTime)
        );
    }

    public GeoLocality findByIdOrFail(Long id) {
        GeoLocality locality = geoLocalityMapper.selectById(id);
        if (Objects.isNull(locality)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("LocalityId not found");
        }
        return locality;
    }

    public List<GeoLocality> listByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return geoLocalityMapper.selectBatchIds(ids);
    }

    public Boolean mergeLocalities(Long sourceLocalityId, Long targetLocalityId) {
        GeoLocality sourceLocality = findByIdOrFail(sourceLocalityId);
        GeoLocality targetLocality = findByIdOrFail(targetLocalityId);

        if (!Objects.equals(sourceLocality.getCountryId(), targetLocality.getCountryId())) {
            throw LocationError.COUNTRIES_ARE_INCONSISTENT.withDefaults();
        }

        List<String> sourceKeywords = Optional.ofNullable(sourceLocality.getKeywords()).orElse(new ArrayList<>());
        List<String> targetKeywords = Optional.ofNullable(targetLocality.getKeywords()).orElse(new ArrayList<>());

        targetKeywords.add(sourceLocality.getLongName());
        targetKeywords.add(sourceLocality.getShortName());
        targetKeywords.addAll(sourceKeywords);

        targetKeywords = targetKeywords.stream().distinct().collect(Collectors.toList());
        targetLocality.setKeywords(targetKeywords);

        geoLocalityMapper.deleteById(sourceLocality);

        return geoLocalityMapper.updateById(targetLocality) > 0;
    }
}
