package com.digcoin.snapx.server.admin.restaurant.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.util.CustomCollectionUtil;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.domain.infra.entity.GeoLocality;
import com.digcoin.snapx.domain.infra.service.GeoCountryService;
import com.digcoin.snapx.domain.infra.service.GeoLocalityService;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import com.digcoin.snapx.domain.restaurant.error.RestaurantError;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantMapper;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewMapper;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantService;
import com.digcoin.snapx.server.admin.member.converter.MemberConverter;
import com.digcoin.snapx.server.admin.restaurant.assembler.BaseRestaurantAssembler;
import com.digcoin.snapx.server.admin.restaurant.dto.command.*;
import com.digcoin.snapx.server.admin.restaurant.dto.command.query.RestaurantFindRestaurantQry;
import com.digcoin.snapx.server.admin.restaurant.dto.command.query.RestaurantPageRestaurantsQry;
import com.digcoin.snapx.server.admin.restaurant.dto.RestaurantDTO;
import com.digcoin.snapx.server.base.infra.assembler.BaseCountryAssembler;
import com.digcoin.snapx.server.base.infra.assembler.BaseLocalityAssembler;
import com.digcoin.snapx.server.base.infra.dto.CountryDTO;
import com.digcoin.snapx.server.base.infra.dto.LocalityDTO;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/25 21:48
 * @description
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminRestaurantService {

    private final RestaurantMapper restaurantMapper;

    private final RestaurantService restaurantService;
    private final BaseRestaurantAssembler baseRestaurantAssembler;

    private final RestaurantReviewService restaurantReviewService;
    private final RestaurantReviewMapper restaurantReviewMapper;

    private final GeoCountryService geoCountryService;
    private final BaseCountryAssembler baseCountryAssembler;

    private final GeoLocalityService geoLocalityService;
    private final BaseLocalityAssembler baseLocalityAssembler;

    private final MemberService memberService;
    private final MemberConverter memberConverter;


    public PageResult<RestaurantDTO> pageRestaurants(RestaurantPageRestaurantsQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getPageSize());
        // List<Restaurant> list = restaurantService.listRestaurants(qry);

        LambdaQueryWrapper<Restaurant> queryWrapper = Wrappers.<Restaurant>lambdaQuery().orderByDesc(Restaurant::getCreateTime);
        if (Objects.nonNull(qry.getId())) {
            queryWrapper.like(Restaurant::getId, qry.getId());
        }
        if (Objects.nonNull(qry.getCountryId())) {
            queryWrapper.eq(Restaurant::getCountryId, qry.getCountryId());
        }
        if (Objects.nonNull(qry.getLocalityId())) {
            queryWrapper.eq(Restaurant::getLocalityId, qry.getLocalityId());
        }
        if (Objects.nonNull(qry.getName())) {
            queryWrapper.and(nameQueryWrapper -> {
                nameQueryWrapper.like(Restaurant::getName, qry.getName());
                nameQueryWrapper.or();
                nameQueryWrapper.like(Restaurant::getNameZhHk, qry.getName());
            });
        }
        if (Objects.nonNull(qry.getGoogle())) {
            queryWrapper.eq(Restaurant::getGoogle, qry.getGoogle());
        }
        if (Objects.nonNull(qry.getSpec())) {
            queryWrapper.eq(Restaurant::getSpec, qry.getSpec());
        }
        if (Objects.nonNull(qry.getVerified())) {
            queryWrapper.eq(Restaurant::getVerified, qry.getVerified());
        }
        if (Objects.nonNull(qry.getWatermark())) {
            queryWrapper.eq(Restaurant::getWatermark, qry.getWatermark());
        }

        List<Restaurant> list = restaurantMapper.selectList(queryWrapper);
        if (list.isEmpty()) {
            return PageResult.emptyResult();
        }

        // 国家
        List<Long> countryIds = CustomCollectionUtil.listColumn(list, Restaurant::getCountryId);
        List<GeoCountry> countryList = geoCountryService.listByIds(countryIds);
        Map<Long, GeoCountry> countryMap = CustomCollectionUtil.mapping(countryList, GeoCountry::getId);

        // 城市
        List<Long> localityIds = CustomCollectionUtil.listColumn(list, Restaurant::getLocalityId);
        List<GeoLocality> localityList = geoLocalityService.listByIds(localityIds);
        Map<Long, GeoLocality> localityMap = CustomCollectionUtil.mapping(localityList, GeoLocality::getId);

        // 会员
        List<Long> memberIds = CustomCollectionUtil.listColumn(list, Restaurant::getCreateBy);
        List<Member> memberList = memberService.listMember(memberIds);
        Map<Long, Member> memberMap = CustomCollectionUtil.mapping(memberList, Member::getId);

        List<RestaurantDTO> voList = list.stream().map(item -> {
            RestaurantDTO restaurantDTO = baseRestaurantAssembler.toDTO(item);

            GeoCountry country = countryMap.get(item.getCountryId());
            if (Objects.nonNull(country)) {
                CountryDTO countryDTO = baseCountryAssembler.toDTO(country);
                restaurantDTO.setCountry(countryDTO);
            }

            GeoLocality locality = localityMap.get(item.getLocalityId());
            if (Objects.nonNull(locality)) {
                LocalityDTO localityDTO = baseLocalityAssembler.toDTO(locality);
                restaurantDTO.setLocality(localityDTO);
            }

            // 会员
            Member member = memberMap.get(item.getCreateBy());
            if (Objects.nonNull(member)) {
                restaurantDTO.setCreator(memberConverter.intoDTO(member));
            }

            return restaurantDTO;
        }).collect(Collectors.toList());
        return PageResult.converter(list, voList);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRestaurant(RestaurantDeleteRestaurantCmd cmd) {
        if (restaurantReviewService.countByRestaurantId(cmd.getId()) > 0) {
            throw RestaurantError.PLEASE_DELETE_THE_ASSOCIATED_REVIEW_FIRST.withDefaults();
        }
        return restaurantService.delete(cmd.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean setSpec(RestaurantSetSpecCmd cmd) {
        // 水印状态跟指定餐厅关联，spec开启 = 水印开启
        restaurantService.enableWatermark(cmd.getId(), cmd.getStatus());
        return restaurantService.setSpecById(cmd.getId(), cmd.getStatus());
    }

    @Transactional(rollbackFor = Exception.class)
    public RestaurantDTO createRestaurant(RestaurantCreateRestaurantCmd cmd) {
        Restaurant restaurant = baseRestaurantAssembler.toEntity(cmd);
        restaurantService.createRestaurant(restaurant);
        restaurant = restaurantService.findByIdOrFail(restaurant.getId());
        return baseRestaurantAssembler.toDTO(restaurant);
    }

    public RestaurantDTO findRestaurant(RestaurantFindRestaurantQry qry) {
        Restaurant restaurant = restaurantService.findByIdOrFail(qry.getId());
        return baseRestaurantAssembler.toDTO(restaurant);
    }

    public RestaurantDTO updateRestaurant(RestaurantUpdateRestaurantCmd cmd) {
        Restaurant restaurant = restaurantService.findByIdOrFail(cmd.getId());
        Restaurant inputRestaurant = baseRestaurantAssembler.toEntity(cmd);
        BeanUtil.copyProperties(inputRestaurant, restaurant, CopyOptions.create().ignoreNullValue());
        restaurantService.updateRestaurant(restaurant);
        return baseRestaurantAssembler.toDTO(restaurant);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean setVerified(RestaurantSetVerifiedCmd cmd) {
        return restaurantService.setVerifiedById(cmd.getId(), cmd.getStatus());
    }

    public Boolean enableWatermark(RestaurantEnableWatermarkCmd cmd) {
        return restaurantService.enableWatermark(cmd.getId(), cmd.getStatus());
    }

}
