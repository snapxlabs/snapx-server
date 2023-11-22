package com.digcoin.snapx.server.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.core.common.enums.Operator;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.domain.system.bo.QrcodeQuery;
import com.digcoin.snapx.server.base.system.dto.QrcodeDTO;
import com.digcoin.snapx.server.base.system.service.InviteCodeAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "注册邀请码管理")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/system/invite-code")
@RequiredArgsConstructor
public class InviteCodeController {

    private final InviteCodeAppService inviteCodeAppService;

    @Operation(description = "分页获取注册邀请码列表")
    @GetMapping("page-invite-code")
    public PageResult<QrcodeDTO> pageInviteCode(QrcodeQuery query) {
        return inviteCodeAppService.pageInviteCode(query);
    }

    @Operation(description = "批量创建邀请码")
    @PostMapping("bath-create-invite-code")
    public void bathCreateInviteCode(@Schema(hidden = true) CurrentUser currentUser,
                                     @RequestParam Integer num,
                                     @RequestParam(required = false) Boolean unlimited) {
        inviteCodeAppService.bathCreateInviteCode(
                Operator.ADMIN,
                currentUser.getId(),
                num,
                Boolean.TRUE.equals(unlimited) ? 0 : null);
    }

}
