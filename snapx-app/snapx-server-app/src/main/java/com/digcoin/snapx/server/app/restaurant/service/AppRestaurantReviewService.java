package com.digcoin.snapx.server.app.restaurant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.enums.SortDirection;
import com.digcoin.snapx.core.common.util.CustomCollectionUtil;
import com.digcoin.snapx.core.dto.SortParamDTO;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.infra.service.InSiteMessageService;
import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.domain.member.service.MemberService;
import com.digcoin.snapx.domain.restaurant.bo.RestaurantReviewPageReviewsQryDTO;
import com.digcoin.snapx.domain.restaurant.bo.ReviewOverviewDTO;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReview;
import com.digcoin.snapx.domain.restaurant.entity.RestaurantReviewLikes;
import com.digcoin.snapx.domain.restaurant.enums.ReviewChangeFromType;
import com.digcoin.snapx.domain.restaurant.error.RestaurantReviewError;
import com.digcoin.snapx.domain.restaurant.event.RestaurantReviewLikedEvent;
import com.digcoin.snapx.domain.restaurant.event.RestaurantReviewedEvent;
import com.digcoin.snapx.domain.restaurant.mapper.RestaurantReviewLikesMapper;
import com.digcoin.snapx.domain.restaurant.service.*;
import com.digcoin.snapx.server.app.restaurant.converter.AppRestaurantReviewLikesConverter;
import com.digcoin.snapx.server.app.restaurant.dto.LikesDTO;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewDTO;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantReviewLikeCmd;
import com.digcoin.snapx.server.app.restaurant.dto.command.RestaurantReviewUpdateCmd;
import com.digcoin.snapx.server.app.restaurant.dto.command.query.PageLikesQry;
import com.digcoin.snapx.server.app.restaurant.dto.command.query.RestaurantReviewFindReviewQry;
import com.digcoin.snapx.server.app.restaurant.vo.RestaurantReviewLikeVO;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/28 10:06
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppRestaurantReviewService {

    private final RestaurantReviewService restaurantReviewService;
    private final RestaurantReviewLikesService restaurantReviewLikesService;
    private final RestaurantReviewChangeService restaurantReviewChangeService;
    private final RestaurantService restaurantService;
    private final MemberService memberService;
    private final AppRestaurantAggregationService appRestaurantAggregationService;
    private final RestaurantReviewLikesMapper restaurantReviewLikesMapper;
    private final AppRestaurantReviewLikesConverter appRestaurantReviewLikesConverter;
    private final InSiteMessageService inSiteMessageService;


    public PageResult<RestaurantReviewDTO> pageReviews(RestaurantReviewPageReviewsQryDTO dto, CurrentUser currentUser) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<RestaurantReview> reviewList = restaurantReviewService.listByRestaurantIdOrLocalityId(dto.getRestaurantId(), dto.getLocalityId(), dto.getRatings(), dto.getOrderBy());
        if (CollectionUtils.isEmpty(reviewList)) {
            return PageResult.converter(reviewList, Collections.emptyList());
        }

        Map<Long, RestaurantReviewDTO> reviewDTOMap = appRestaurantAggregationService.mappingReviewDTO(reviewList, currentUser);

        List<RestaurantReviewDTO> voList =
                reviewList.stream().map(item -> reviewDTOMap.get(item.getId())).collect(Collectors.toList());
        return PageResult.converter(reviewList, voList);
    }

    public PageResult<LikesDTO> pageLikes(PageLikesQry qry) {
        LambdaQueryWrapper<RestaurantReviewLikes> queryWrapper = Wrappers.<RestaurantReviewLikes>lambdaQuery().eq(RestaurantReviewLikes::getReviewId, qry.getReviewId());

        // 排序
        List<SortParamDTO> orderBy = Optional.ofNullable(qry.getOrderBy()).orElse(Collections.emptyList());
        for (SortParamDTO sortParamDTO : orderBy) {
            // 按点赞时间
            if (PageLikesQry.OrderByField.CREATE_TIME.getField().equals(sortParamDTO.getField())) {
                SortDirection direction = Optional.ofNullable(sortParamDTO.getDirection()).orElse(SortDirection.DESC);
                queryWrapper.orderBy(true, SortDirection.ASC.equals(direction), RestaurantReviewLikes::getCreateTime);
            }
        }
        // 默认排序
        if (queryWrapper.getExpression().getOrderBy().isEmpty()) {
            queryWrapper.orderByDesc(RestaurantReviewLikes::getCreateTime);
        }

        PageHelper.startPage(qry.getPage(), qry.getPageSize());
        List<RestaurantReviewLikes> list = restaurantReviewLikesMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.converter(list, Collections.emptyList());
        }

        // 用户信息
        List<Long> memberIds = CustomCollectionUtil.listColumn(list, RestaurantReviewLikes::getMemberId);
        Map<Long, Member> memberMap = memberService.mappingMember(memberIds);

        List<LikesDTO> dtoList = list.stream().map(item -> {
            LikesDTO dto = appRestaurantReviewLikesConverter.toDTO(item);

            // 用户信息
            Member member = memberMap.getOrDefault(item.getMemberId(), new Member());
            dto.setMemberId(member.getId());
            dto.setMemberNickname(member.getNickname());
            dto.setMemberAvatar(member.getAvatar());

            return dto;
        }).collect(Collectors.toList());

        return PageResult.converter(list, dtoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public RestaurantReviewLikeVO like(RestaurantReviewLikeCmd cmd, CurrentUser currentUser) {
        RestaurantReview review = restaurantReviewService.findReviewByIdOrFail(cmd.getReviewId());
        Boolean likes = restaurantReviewLikesService.like(review, cmd.getLikes(), currentUser.getId());
        Long likesNum = restaurantReviewService.updateLikesNum(review.getId());
        restaurantReviewService.publishRestaurantReviewLikedEvent(review.getId(), cmd.getLikes(), currentUser.getId());
        sendThumbsUpNotify(cmd, currentUser, review);
        return new RestaurantReviewLikeVO(likes, likesNum);
    }

    private void sendThumbsUpNotify(RestaurantReviewLikeCmd cmd, CurrentUser currentUser, RestaurantReview review) {
        if (!cmd.getLikes() || Objects.equals(currentUser.getId(), review.getMemberId())) {
            // 取消点赞，自己给自己点赞不发送站内信通知
            return;
        }
        try {
            inSiteMessageService.sendThumbsUpNotify(review.getMemberId(), currentUser.getNickname(), review.getId());
        } catch (Exception e) {
            log.error("sendThumbsUpNotify fail cmd:[{}] currentUser:[{}] review:[{}]", cmd, currentUser, review, e);
        }
    }

    @EventListener
    public void handleRestaurantReviewedEvent(RestaurantReviewedEvent event) {
        log.info("receive a review event", event);
    }

    @EventListener
    public void handleRestaurantReviewLikedEvent(RestaurantReviewLikedEvent event) {
        log.info("receive a likes event", event);
    }

    public RestaurantReviewDTO findReview(RestaurantReviewFindReviewQry dto, CurrentUser currentUser) {
        RestaurantReview review = restaurantReviewService.findReviewByIdOrFail(dto.getReviewId());
        Map<Long, RestaurantReviewDTO> mappingReview = appRestaurantAggregationService.mappingReviewDTO(Arrays.asList(review), currentUser);
        return mappingReview.get(review.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public RestaurantReviewDTO updateRateAndContent(RestaurantReviewUpdateCmd cmd, CurrentUser currentUser) {
        RestaurantReview review = restaurantReviewService.findByIdOrFail(cmd.getReviewId());
        if (!Objects.equals(review.getMemberId(), currentUser.getId())) {
            throw RestaurantReviewError.YOU_CANT_MODIFY_THE_CURRENT_REVIEW.withDefaults();
        }

        // 记录
        restaurantReviewChangeService.tryRecordChangeForRateAndContent(review, cmd.getRating(), cmd.getContent(),
                ReviewChangeFromType.MEMBER, currentUser.getId());
        // 修改
        restaurantReviewService.updateRateAndContent(review, cmd.getRating(), cmd.getContent());
        // 更新统计
        ReviewOverviewDTO reviewOverviewDTO = restaurantReviewService.countReviewOverview(review.getRestaurantId());
        restaurantService.updateReviewOverview(review.getRestaurantId(), reviewOverviewDTO);

        Map<Long, RestaurantReviewDTO> mappingReview = appRestaurantAggregationService.mappingReviewDTO(Arrays.asList(review), currentUser);
        return mappingReview.get(review.getId());
    }

}
