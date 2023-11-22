package com.digcoin.snapx.server.base.restaurant.converter;

import com.digcoin.snapx.domain.restaurant.entity.Journal;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 12:24
 * @description
 */
@Mapper(componentModel = "spring")
public interface BaseRestaurantReviewConverter {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "id", target = "reviewId")
    Journal review2Journal(RestaurantReview review);

    @AfterMapping
    default void review2Journal(RestaurantReview review, @MappingTarget Journal journal) {
        LocalDate reviewDate = review.getCreateTime().toLocalDate();
        journal.setDate(reviewDate);
        journal.setYear(Long.valueOf(reviewDate.getYear()));
        journal.setMonth(Long.valueOf(reviewDate.getMonthValue()));
        journal.setDay(Long.valueOf(reviewDate.getDayOfMonth()));
    }

}
