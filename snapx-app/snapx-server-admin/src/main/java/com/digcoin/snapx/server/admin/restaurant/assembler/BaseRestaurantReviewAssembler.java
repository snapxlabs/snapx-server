package com.digcoin.snapx.server.admin.restaurant.assembler;

import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.server.admin.restaurant.dto.RestaurantReviewDTO;
import com.digcoin.snapx.server.admin.restaurant.dto.command.RestaurantReviewCreateReviewCmd;
import com.google.common.base.Joiner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/27 21:26
 * @description
 */
@Mapper(componentModel = "spring")
public interface BaseRestaurantReviewAssembler {

    @Mapping(source = "photoUrls", target = "photoUrls", qualifiedByName = "urlsSeparator")
    RestaurantReviewDTO toDTO(RestaurantReview item);

    @Named("urlsSeparator")
    default List<String> urlsSeparator(String input) {
        List<String> output = new ArrayList<>();
        if (input != null) {
            String[] parts = input.split(",");
            for (String part : parts) {
                output.add(part.trim());
            }
        }
        return output;
    }

    @Named("urlsSeparator")
    default String urlsSeparator(List<String> urls) {
        return Joiner.on(",").join(urls);
    }

    @Mapping(source = "photoUrls", target = "photoUrls", qualifiedByName = "urlsSeparator")
    RestaurantReview toDataObject(RestaurantReviewCreateReviewCmd cmd);

}
