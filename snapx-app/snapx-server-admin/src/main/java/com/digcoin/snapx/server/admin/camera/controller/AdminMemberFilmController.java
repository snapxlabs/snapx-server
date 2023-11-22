package com.digcoin.snapx.server.admin.camera.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.admin.camera.dto.AdminFilmMemberAdjustDTO;
import com.digcoin.snapx.server.admin.camera.dto.AdminFilmMemberDetailPageDTO;
import com.digcoin.snapx.server.admin.camera.dto.AdminFilmMemberPageDTO;
import com.digcoin.snapx.server.admin.camera.service.AdminMemberFilmService;
import com.digcoin.snapx.server.admin.camera.vo.AdminFilmMemberDetailVO;
import com.digcoin.snapx.server.admin.camera.vo.AdminFilmMemberVO;
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
 * @date 2023/3/22 1:16
 * @description
 */
@Tag(name = "603 - 会员胶卷 - 会员胶卷")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/film-member")
@RequiredArgsConstructor
public class AdminMemberFilmController {

    private final AdminMemberFilmService adminMemberFilmService;

    @Operation(summary = "会员胶卷分页")
    @GetMapping("/page")
    public PageResult<AdminFilmMemberVO> pageFilmMember(AdminFilmMemberPageDTO dto) {
        return adminMemberFilmService.pageFilmMember(dto);
    }

    @Operation(summary = "会员胶卷明细分页")
    @GetMapping("/detail/page")
    public PageResult<AdminFilmMemberDetailVO> pageFilmMemberDetail(@Validated AdminFilmMemberDetailPageDTO dto) {
        return adminMemberFilmService.pageFilmMemberDetail(dto);
    }

    @Operation(summary = "调整当前会员胶卷")
    @PostMapping("/adjust-quantity")
    public void adjustFilmMemberQuantity(@Schema(hidden = true) CurrentUser currentUser,
                                         @Validated @RequestBody AdminFilmMemberAdjustDTO dto) {
        dto.setAdminUserId(currentUser.getId());
         adminMemberFilmService.adjustFilmMemberQuantity(dto);
    }


}
