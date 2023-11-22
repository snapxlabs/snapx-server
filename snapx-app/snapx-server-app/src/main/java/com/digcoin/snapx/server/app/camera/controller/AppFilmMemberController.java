package com.digcoin.snapx.server.app.camera.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.app.camera.dto.FilmMemberQuantityReduceDTO;
import com.digcoin.snapx.server.app.camera.service.AppFilmMemberService;
import com.digcoin.snapx.server.app.camera.vo.AppFilmMemberVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 14:28
 * @description
 */
@Tag(name = "603 - 胶卷 - 会员胶卷信息")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/film-member")
@RequiredArgsConstructor
public class AppFilmMemberController {

    private final AppFilmMemberService appFilmMemberService;

    @Operation(summary = "获取当前会员胶卷余额及变动明细")
    @GetMapping("/get")
    public AppFilmMemberVO getFilmMember(@Schema(hidden = true) CurrentUser currentUser) {
        return appFilmMemberService.getFilmMember(currentUser.getId());
    }

    @Operation(summary = "扣减当前会员胶卷")
    @PostMapping("/reduce-quantity")
    public AppFilmMemberVO reduceFilmMemberQuantity(@Schema(hidden = true) CurrentUser currentUser,
                                                  @Validated @RequestBody FilmMemberQuantityReduceDTO dto) {
        dto.setMemberId(currentUser.getId());
        return appFilmMemberService.reduceFilmMemberQuantity(dto);
    }

}
