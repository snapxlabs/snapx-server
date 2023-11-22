package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.common.util.CustomCollectionUtil;
import com.digcoin.snapx.domain.infra.bo.LocationBO;
import com.digcoin.snapx.domain.infra.component.googlemap.GeoApiContextFactory;
import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.domain.infra.entity.GeoLocality;
import com.digcoin.snapx.domain.infra.error.LocationError;
import com.digcoin.snapx.domain.infra.mapper.GeoCountryMapper;
import com.digcoin.snapx.domain.infra.mapper.GeoLocalityMapper;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 20:38
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final GeoApiContextFactory geoApiContextFactory;
    private final GeoCountryMapper geoCountryMapper;
    private final GeoLocalityMapper geoLocalityMapper;

    public GeoCountry findCountryById(Long countryId) {
        return geoCountryMapper.selectById(countryId);
    }

    public GeoCountry findCountryByIdOrFail(Long countryId) {
        GeoCountry country = findCountryById(countryId);
        if (Objects.isNull(country)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("CountryId not found");
        }
        return country;
    }

    public GeoLocality findLocalityById(Long localityId) {
        return geoLocalityMapper.selectById(localityId);
    }

    public GeoLocality findLocalityByIdOrFail(Long localityId) {
        GeoLocality locality = findLocalityById(localityId);
        if (Objects.isNull(locality)) {
            throw CommonError.DATA_NOT_EXIST.withMessage("LocalityId not found");
        }
        return locality;
    }

    public LocationBO getSystemLocationByLocalityId(Long localityId) {
        GeoLocality locality = findLocalityByIdOrFail(localityId);
        GeoCountry country = findCountryByIdOrFail(locality.getCountryId());
        return new LocationBO(country, locality);
    }

    /**
     * 通过经纬度获取系统位置
     *
     * @param lat
     * @param lng
     * @return
     */
    public LocationBO getSystemLocationByLatLng(BigDecimal lat, BigDecimal lng) {
        List<AddressComponent> addressComponents = getGeoAddressByLatLng(geoApiContextFactory.getDefault(), new LatLng(lat.doubleValue(), lng.doubleValue()));
        AddressComponent country = addressComponents.get(0);
        AddressComponent locality = addressComponents.get(1);

        List<AddressComponent> addressComponentsZhHk = getGeoAddressByLatLng(geoApiContextFactory.get(GeoApiContextFactory.LANGUAGE_ZH_HK), new LatLng(lat.doubleValue(), lng.doubleValue()));
        AddressComponent countryZhHk = addressComponentsZhHk.get(0);
        AddressComponent localityZhHk = addressComponentsZhHk.get(1);

        // 国家
        GeoCountry geoCountry = geoCountryMapper.selectOne(Wrappers.lambdaQuery(GeoCountry.class)
                .eq(GeoCountry::getLongName, country.longName)
                .eq(GeoCountry::getShortName, country.shortName)
        );
        if (Objects.isNull(geoCountry)) {
            geoCountry = new GeoCountry();
            geoCountry.setLongName(country.longName);
            geoCountry.setShortName(country.shortName);
            geoCountry.setLongNameZhHk(countryZhHk.longName);
            geoCountry.setShortNameZhHk(countryZhHk.shortName);
            geoCountryMapper.insert(geoCountry);
        }

        // 地点
        // List<String> localityKeywords = Arrays.asList(locality.longName, locality.shortName);
        GeoLocality geoLocality = geoLocalityMapper.selectOne(Wrappers.<GeoLocality>lambdaQuery()
                .eq(GeoLocality::getCountryId, geoCountry.getId())
                .and(queryWrapper -> {
                    queryWrapper.or(queryWrapper2 -> {
                        queryWrapper2.eq(GeoLocality::getLongName, locality.longName);
                        queryWrapper2.eq(GeoLocality::getShortName, locality.shortName);
                    });
                    queryWrapper.or(queryWrapper2 -> {
                        queryWrapper2.like(GeoLocality::getKeywords, locality.longName);
                        queryWrapper2.or();
                        queryWrapper2.like(GeoLocality::getKeywords, locality.shortName);
                        queryWrapper2.or();
                        queryWrapper2.like(GeoLocality::getKeywords, localityZhHk.longName);
                        queryWrapper2.or();
                        queryWrapper2.like(GeoLocality::getKeywords, localityZhHk.shortName);
                        // String clo = String.format("JSON_OVERLAPS( JSON_ARRAY('%s'), keywords)", String.join("','", localityKeywords));
                        // queryWrapper2.gtSql((SFunction<GeoLocality, Object>) geoLocality1 -> clo, "0");
                    });
                })
        );
        if (Objects.isNull(geoLocality)) {
            geoLocality = new GeoLocality();
            geoLocality.setCountryId(geoCountry.getId());
            geoLocality.setLongName(locality.longName);
            geoLocality.setShortName(locality.shortName);
            geoLocality.setLongNameZhHk(localityZhHk.longName);
            geoLocality.setShortNameZhHk(localityZhHk.shortName);
            List<String> keywords = new ArrayList<>();
            keywords.addAll(Arrays.asList(locality.longName, locality.shortName, localityZhHk.longName, localityZhHk.shortName));
            keywords = keywords.stream().distinct().collect(Collectors.toList());
            geoLocality.setKeywords(keywords);
            geoLocalityMapper.insert(geoLocality);
        }

        return new LocationBO(geoCountry, geoLocality);
    }

    /**
     * 获取位置
     *
     * @param latLng 经纬度
     * @return 位置信息
     */
    public List<AddressComponent> getGeoAddressByLatLng(LatLng latLng) {
        return getGeoAddressByLatLng(geoApiContextFactory.getDefault(), latLng);
    }

    /**
     * 获取位置
     *
     * @param latLng 经纬度
     * @return 位置信息
     */
    public List<AddressComponent> getGeoAddressByLatLng(GeoApiContext context, LatLng latLng) {
        GeocodingApiRequest req = GeocodingApi.reverseGeocode(context, latLng);

        GeocodingResult[] results;
        try {
            results = req.await();
        } catch (Exception e) {
            log.error("Request GeocodingApi failed: ", e);
            throw LocationError.UNABLE_TO_LOCATE.withDefaults();
        }

        Map<AddressType, AddressComponent> map = toMap(Arrays.asList(results));
        AddressComponent country = map.get(AddressType.COUNTRY);
        AddressComponent locality = map.get(AddressType.LOCALITY);
        if (Objects.isNull(country)) {
            throw LocationError.UNABLE_TO_LOCATE.withDefaults();
        }
        if (Objects.isNull(locality)) {
            locality = map.get(AddressType.ADMINISTRATIVE_AREA_LEVEL_1);
            if (Objects.isNull(locality)) {
                locality = country;
            }
        }

        return Arrays.asList(country, locality);
    }

    /**
     * 转 MAP
     *
     * @param list 列表
     * @return 结果
     */
    private Map<AddressType, AddressComponent> toMap(List<GeocodingResult> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Map<AddressType, AddressComponent> map = new HashMap<>(16);
        GeocodingResult result = list.get(0);
        for (AddressComponent addressComponent : result.addressComponents) {
            AddressComponentType type = addressComponent.types[0];
            if (type.toString().equals(AddressType.COUNTRY.toString())) {
                map.put(AddressType.COUNTRY, addressComponent);
            }
            if (type.toString().equals(AddressType.LOCALITY.toString())) {
                map.put(AddressType.LOCALITY, addressComponent);
            }
            if (type.toString().equals(AddressType.ADMINISTRATIVE_AREA_LEVEL_1.toString())) {
                map.put(AddressType.ADMINISTRATIVE_AREA_LEVEL_1, addressComponent);
            }
        }

        return map;
    }

    public List<LocationBO> listLocalities(String keyword, boolean rec, BigDecimal lat, BigDecimal lng) {

        Long currLocalityId = null;
        if (!Arrays.asList(lat, lng).contains(null)) {
            LocationBO locationBO = null;
            try {
                locationBO = getSystemLocationByLatLng(lat, lng);
            } catch (Exception e) {
            }
            if (Objects.nonNull(locationBO)) {
                currLocalityId = locationBO.getLocality().getId();
            }
        }

        LambdaQueryWrapper<GeoLocality> queryWrapper = Wrappers.lambdaQuery();

        // 推荐城市
        if (rec) {
            queryWrapper.and(wrapper -> {
                wrapper.eq(GeoLocality::getRec, true);
            });
            queryWrapper.orderByAsc(GeoLocality::getWeigh);
        }

        // 搜索
        if (Objects.nonNull(keyword)) {
            String keywordZhHk = ZhConverterUtil.toTraditional(keyword);
            queryWrapper.and(geoLocalityLambdaQueryWrapper -> {
                geoLocalityLambdaQueryWrapper.or(geoLocalityLambdaQueryWrapper1 -> {
                    geoLocalityLambdaQueryWrapper1.like(GeoLocality::getLongName, keyword);
                    geoLocalityLambdaQueryWrapper1.or();
                    geoLocalityLambdaQueryWrapper1.like(GeoLocality::getShortName, keyword);
                    geoLocalityLambdaQueryWrapper1.or();
                    geoLocalityLambdaQueryWrapper1.like(GeoLocality::getKeywords, keyword);
                });
                geoLocalityLambdaQueryWrapper.or(geoLocalityLambdaQueryWrapper1 -> {
                    geoLocalityLambdaQueryWrapper1.like(GeoLocality::getLongNameZhHk, keywordZhHk);
                    geoLocalityLambdaQueryWrapper1.or();
                    geoLocalityLambdaQueryWrapper1.like(GeoLocality::getShortNameZhHk, keywordZhHk);
                    geoLocalityLambdaQueryWrapper1.or();
                    geoLocalityLambdaQueryWrapper1.like(GeoLocality::getKeywords, keywordZhHk);
                });
            });
        }

        // 定位城市
        final Long finalCurrLocalityId = currLocalityId;
        if (Objects.nonNull(currLocalityId)) {
            queryWrapper.or(wrapper -> {
                wrapper.eq(GeoLocality::getId, finalCurrLocalityId);
            });
        }

        // 查询
        List<GeoLocality> localities = geoLocalityMapper.selectList(queryWrapper);

        // 定位城市置顶
        localities.sort((o1, o2) -> {
            if (Objects.equals(o1.getId(), finalCurrLocalityId)) {
                return -1;
            }
            return 0;
        });

        List<Long> countryIds = CustomCollectionUtil.listColumn(localities, GeoLocality::getCountryId);
        if (CollectionUtils.isEmpty(countryIds)) {
            return Collections.emptyList();
        }
        List<GeoCountry> countries = geoCountryMapper.selectList(Wrappers.<GeoCountry>lambdaQuery().in(GeoCountry::getId, countryIds));
        Map<Long, GeoCountry> countryMap = CustomCollectionUtil.mapping(countries, GeoCountry::getId);
        return localities.stream().map(locality -> {
            GeoCountry country = countryMap.get(locality.getCountryId());
            return new LocationBO(country, locality);
        }).collect(Collectors.toList());
    }
}
