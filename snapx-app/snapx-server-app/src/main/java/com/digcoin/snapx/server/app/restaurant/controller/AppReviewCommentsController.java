package com.digcoin.snapx.server.app.restaurant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.app.restaurant.dto.RestaurantReviewCommentsDTO;
import com.digcoin.snapx.server.app.restaurant.service.AppReviewCommentsService;
import com.digcoin.snapx.server.app.restaurant.vo.RestaurantReviewCommentsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/22 16:04
 * @description
 */
@Tag(name = "358 - 评价留言")
@SaCheckLogin
@RequiredArgsConstructor
@RestController
@RequestMapping("/ret/restaurant/review-comments")
public class AppReviewCommentsController {

    private final AppReviewCommentsService appReviewCommentsService;

    @Operation(summary = "提交留言")
    @PostMapping("/create")
    public void createRestaurantReviewComments(@Schema(hidden = true) CurrentUser currentUser,
                                               @Valid @RequestBody RestaurantReviewCommentsDTO dto) {
        dto.setFromMemberId(currentUser.getId());
        dto.setFromMemberName(currentUser.getNickname());
        appReviewCommentsService.createRestaurantReviewComments(dto);
    }

    @Operation(summary = "获取一条评论下的所有留言，按照留言时间升序")
    @GetMapping("/list-by-review/{reviewId}")
    public List<RestaurantReviewCommentsVO> listReviewComments(@Valid @NotNull(message = "Review id cannot be null ")
                                                               @Schema(description = "评论Id", required = true) @PathVariable("reviewId") Long reviewId) {
        return appReviewCommentsService.listReviewComments(reviewId);
    }

    @Operation(summary = "删除留言")
    @PostMapping("/delete/{commentsId}")
    public void deleteReviewComments(@Valid @NotNull(message = "Comments Id cannot be null")
                                     @Schema(description = "留言Id", required = true)
                                     @PathVariable("commentsId") Long commentsId,
                                     @Schema(hidden = true) CurrentUser currentUser) {
        appReviewCommentsService.deleteReviewComments(commentsId, currentUser.getId());
    }
}
