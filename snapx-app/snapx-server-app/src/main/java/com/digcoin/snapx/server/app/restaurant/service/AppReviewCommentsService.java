package com.digcoin.snapx.server.app.restaurant.service;

import com.digcoin.snapx.domain.infra.service.InSiteMessageService;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewComments;
import com.digcoin.snapx.domain.restaurant.error.RestaurantReviewCommentsError;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewCommentsService;
import com.digcoin.snapx.domain.restaurant.service.RestaurantReviewService;
import com.digcoin.snapx.server.app.restaurant.converter.AppReviewCommentsConverter;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewCommentsDTO;
import com.digcoin.snapx.server.app.restaurant.vo.RestaurantReviewCommentsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/24 22:57
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppReviewCommentsService {
    private final RestaurantReviewService restaurantReviewService;

    private final RestaurantReviewCommentsService restaurantReviewCommentsService;

    private final AppReviewCommentsConverter appReviewCommentsConverter;

    private final MemberService memberService;

    private final InSiteMessageService inSiteMessageService;

    @Transactional(rollbackFor = Exception.class)
    public void createRestaurantReviewComments(RestaurantReviewCommentsDTO dto) {
        RestaurantReviewComments restaurantReviewComments = appReviewCommentsConverter.intoDTO(dto);
        RestaurantReview restaurantReview = restaurantReviewService.findByIdOrFail(restaurantReviewComments.getReviewId());
        Long toMemberId;
        if (Objects.nonNull(restaurantReviewComments.getParentCommentsId())) {
            Optional<RestaurantReviewComments> optional = restaurantReviewCommentsService.findById(restaurantReviewComments.getParentCommentsId());
            if(optional.isEmpty()){
                throw RestaurantReviewCommentsError.REVIEW_COMMENTS_NOT_EXISTS.withDefaults();
            }
            RestaurantReviewComments parentComments = optional.get();
            toMemberId = parentComments.getFromMemberId();
            restaurantReviewComments.setParentMemberId(toMemberId);
        }else {
            toMemberId = restaurantReview.getMemberId();
        }
        restaurantReviewCommentsService.createRestaurantReviewComments(restaurantReviewComments);
        inSiteMessageService.sendNewComment(toMemberId, dto.getFromMemberName(), restaurantReviewComments.getReviewId());
    }

    public List<RestaurantReviewCommentsVO> listReviewComments(Long reviewId) {
        List<RestaurantReviewComments> restaurantReviewComments = restaurantReviewCommentsService.listByReviewId(reviewId);
        if (CollectionUtils.isEmpty(restaurantReviewComments)) {
            return Collections.emptyList();
        }
        Set<Long> memberIds = restaurantReviewComments.stream().map(
                RestaurantReviewComments::getFromMemberId).collect(Collectors.toSet());

        memberIds.addAll(restaurantReviewComments.stream().map(
                RestaurantReviewComments::getParentMemberId).filter(Objects::nonNull).collect(Collectors.toSet()));

        Map<Long, Member> memberMap = memberService.listMember(memberIds)
                .stream().collect(Collectors.toMap(Member::getId, Function.identity()));
        return restaurantReviewComments.stream().map(entity -> {
                    RestaurantReviewCommentsVO vo = appReviewCommentsConverter.intoVO(entity);
                    if (Objects.nonNull(vo.getParentMemberId())) {
                        Optional.ofNullable(memberMap.get(vo.getParentMemberId())).ifPresent(m -> vo.setParentMemberName(
                                memberMap.get(vo.getParentMemberId()).getNickname()));
                    }
                    Optional.ofNullable(memberMap.get(vo.getFromMemberId())).ifPresent(m -> vo.setFromMemberName(
                            memberMap.get(vo.getFromMemberId()).getNickname()));
                    return vo;
                }
        ).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteReviewComments(Long commentsId, Long memberId) {
        RestaurantReviewComments restaurantReviewComments = restaurantReviewCommentsService.findById(commentsId).orElseThrow(
                RestaurantReviewCommentsError.REVIEW_COMMENTS_NOT_EXISTS::withDefaults);
        RestaurantReview restaurantReview = restaurantReviewService.findByIdOrFail(restaurantReviewComments.getReviewId());
        if(!memberId.equals(restaurantReviewComments.getFromMemberId()) && !memberId.equals(restaurantReview.getMemberId())){
            throw RestaurantReviewCommentsError.REVIEW_COMMENTS_CANNOT_DELETE.withDefaults();
        }
        restaurantReviewCommentsService.deleteReviewComments(commentsId);
    }
}
