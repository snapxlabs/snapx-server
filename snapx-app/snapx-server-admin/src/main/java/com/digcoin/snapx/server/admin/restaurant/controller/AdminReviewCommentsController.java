package com.digcoin.snapx.server.admin.restaurant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.server.admin.restaurant.dto.AdminReviewCommentsPageDTO;
import com.digcoin.snapx.server.admin.restaurant.service.AdminReviewCommentsService;
import com.digcoin.snapx.server.admin.restaurant.vo.AdminReviewCommentsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/27 19:05
 * @description
 */
@Tag(name = "230328 - 餐厅评价留言")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/restaurant/review-comments")
@RequiredArgsConstructor
public class AdminReviewCommentsController {

   private final AdminReviewCommentsService adminReviewCommentsService;

    @Operation(summary = "餐厅评价留言分页")
    @GetMapping("/page")
    public PageResult<AdminReviewCommentsVO> pageReviewComments(@Valid AdminReviewCommentsPageDTO dto) {
        return adminReviewCommentsService.pageReviewComments(dto);
    }

    @Operation(summary = "删除留言")
    @PostMapping("/delete/{id}")
    public void deleteReviewComments(@Valid @NotNull(message = "Comments Id cannot be null")
                             @Schema(description = "留言Id", required = true)
                             @PathVariable("id") Long id) {
        adminReviewCommentsService.deleteReviewComments(id);
    }

}
