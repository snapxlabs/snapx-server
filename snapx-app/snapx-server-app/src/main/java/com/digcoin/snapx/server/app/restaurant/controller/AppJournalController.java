package com.digcoin.snapx.server.app.restaurant.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.restaurant.bo.JournalPageDailyJournalsQryDTO;
import com.digcoin.snapx.domain.restaurant.bo.JournalPageJournalsQryDTO;
import com.digcoin.snapx.server.app.restaurant.service.AppJournalService;
import com.digcoin.snapx.server.app.restaurant.vo.JournalPageDailyJournalsVO;
import com.digcoin.snapx.server.app.restaurant.vo.JournalPageJournalsVO;
import com.digcoin.snapx.server.base.member.dto.BaseTotalGiftCountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 17:18
 * @description
 */
@Tag(name = "331 - 日记")
@SaCheckLogin
@RequiredArgsConstructor
@RequestMapping("/ret/journal")
@RestController
public class AppJournalController {

    private final AppJournalService appJournalService;

    @Operation(summary = "分页查询日记列表")
    @GetMapping("page-journals")
    public PageResult<JournalPageJournalsVO> pageJournals(@Schema(hidden = true) CurrentUser currentUser, JournalPageJournalsQryDTO dto) {
        return appJournalService.pageJournals(dto, currentUser);
    }

    @Operation(summary = "分页查询每日日记列表")
    @GetMapping("page-daily-journals")
    public PageResult<JournalPageDailyJournalsVO> pageDailyJournals(@Schema(hidden = true) CurrentUser currentUser, JournalPageDailyJournalsQryDTO dto) {
        return appJournalService.pageDailyJournals(dto, currentUser);
    }

    @Operation(summary = "获取所有参与评价日记已获取总数量")
    @GetMapping("/gift-total")
    public BaseTotalGiftCountDTO getJournalGiftTotal(@Schema(hidden = true) CurrentUser currentUser) {
        return appJournalService.getJournalGiftTotal(currentUser.getId());
    }
}
