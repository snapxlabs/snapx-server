package com.digcoin.snapx.server.app.restaurant.converter;

import com.digcoin.snapx.core.common.util.DistanceCalculator;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantBO;
import com.digcoin.snapx.domain.restaurant.entity.Restaurant;
import com.digcoin.snapx.domain.system.constant.EarningCurrencyUnit;
import com.digcoin.snapx.server.app.restaurant.dto.command.OrderCreateOrderCmd;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantCreateRestaurantCmd;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantDTO;
import com.google.maps.model.Photo;
import com.google.maps.model.PlacesSearchResult;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/2 21:27
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppRestaurantConverter {

    @Mapping(source = "spec", target = "watermark")
    RestaurantDTO restaurant2DTO(Restaurant restaurant);

    @AfterMapping
    default void restaurant2DTO(Restaurant restaurant, @MappingTarget RestaurantDTO restaurantDTO) {
        restaurantDTO.setAvgEarnedUnit(EarningCurrencyUnit.EXP);
    }

    @AfterMapping
    default void restaurant2DTO(RestaurantBO restaurant, @MappingTarget RestaurantDTO restaurantDTO) {
        restaurantDTO.setAvgEarnedUnit(EarningCurrencyUnit.EXP);
    }

    @Mapping(source = "latLng.lat", target = "lat")
    @Mapping(source = "latLng.lng", target = "lng")
    Restaurant fromDTO(RestaurantCreateRestaurantCmd cmd);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id", target = "placeId")
    Restaurant fromPlaceDTO(OrderCreateOrderCmd.PlaceDTO cmd);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "place.id", target = "placeId")
    @Mapping(source = "place.name", target = "name")
    @Mapping(source = "place.nameZhHk", target = "nameZhHk")
    @Mapping(source = "place.coverUrl", target = "coverUrl")
    @Mapping(source = "place.photoReference", target = "photoReference")
    @Mapping(source = "place.photoWidth", target = "photoWidth")
    @Mapping(source = "place.photoHeight", target = "photoHeight")
    @Mapping(source = "latLng.lat", target = "lat")
    @Mapping(source = "latLng.lng", target = "lng")
    Restaurant fromPlaceDTO(OrderCreateOrderCmd cmd);

    // @Mapping(source = "icon", target = "coverUrl")
    @Mapping(source = "geometry.location.lat", target = "lat")
    @Mapping(source = "geometry.location.lng", target = "lng")
    @Mapping(source = "photos", target = "photoReference", qualifiedByName = "convertToPhotoUrl")
    @Mapping(source = "photos", target = "photoWidth", qualifiedByName = "convertToPhotoWidth")
    @Mapping(source = "photos", target = "photoHeight", qualifiedByName = "convertToPhotoHeight")
    RestaurantDTO toDTO(PlacesSearchResult result);


    List<RestaurantDTO> toDTO(List<PlacesSearchResult> results);

    default void calcDistance(BigDecimal lat, BigDecimal lng, RestaurantDTO restaurantDTO) {
        if (Objects.isNull(restaurantDTO)) {
            return;
        }

        if (Objects.isNull(restaurantDTO.getLat()) || Objects.isNull(restaurantDTO.getLng())) {
            return;
        }

        double distance = DistanceCalculator.distance(lat.doubleValue(), lng.doubleValue(), restaurantDTO.getLat().doubleValue(), restaurantDTO.getLng().doubleValue());
        restaurantDTO.setDistance(new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP));
    }

    @Named("convertToPhotoUrl")
    default String convertToPhotoUrl(Photo[] photos) {
        if (Objects.isNull(photos) || photos.length == 0) {
            return null;
        }
        return photos[0].photoReference;
    }

    @Named("convertToPhotoWidth")
    default Integer convertToPhotoWidth(Photo[] photos) {
        if (Objects.isNull(photos) || photos.length == 0) {
            return null;
        }
        return photos[0].width;
    }

    @Named("convertToPhotoHeight")
    default Integer convertToPhotoHeight(Photo[] photos) {
        if (Objects.isNull(photos) || photos.length == 0) {
            return null;
        }
        return photos[0].height;
    }

}
