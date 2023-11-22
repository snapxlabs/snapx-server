package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.domain.infra.mapper.GeoCountryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 16:07
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeoCountryService {

    private final GeoCountryMapper geoCountryMapper;

    public List<GeoCountry> listCountries() {
        return geoCountryMapper.selectList(Wrappers.<GeoCountry>lambdaQuery()
                .orderByDesc(GeoCountry::getCreateTime)
        );
    }

    public GeoCountry findByIdOrFail(Long id) {
        GeoCountry country = geoCountryMapper.selectById(id);
        if (Objects.isNull(country)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("CountryId not found");
        }
        return country;
    }

    public List<GeoCountry> listByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return geoCountryMapper.selectBatchIds(ids);
    }

}
