package com.digcoin.snapx.server.admin.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.digcoin.snapx.server.base.member.dto.MemberAvatarDTO;
import com.digcoin.snapx.server.base.member.dto.MemberAvatarDeleteDTO;
import com.digcoin.snapx.server.base.member.service.MemberAvatarAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户头像管理")
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping("/system/member-avatar")
@RequiredArgsConstructor
public class MemberAvatarController {

    private final MemberAvatarAppService memberAvatarAppService;

    @Operation(summary = "获取用户备选头像列表")
    @GetMapping("list-member-avatar")
    public List<MemberAvatarDTO> listMemberAvatar() {
        return memberAvatarAppService.listMemberAvatar();
    }

    @Operation(summary = "创建用户备选头像")
    @PostMapping("create-member-avatar")
    public void createMemberAvatar(@RequestBody MemberAvatarDTO memberAvatar) {
        memberAvatarAppService.createMemberAvatar(memberAvatar);
    }

    @Operation(summary = "创建用户备选头像")
    @PostMapping("update-member-avatar")
    public void updateMemberAvatar(@RequestBody MemberAvatarDTO memberAvatar) {
        memberAvatarAppService.updateMemberAvatar(memberAvatar);
    }

    @Operation(summary = "删除用户备选头像")
    @PostMapping("delete-member-avatar")
    public void deleteMemberAvatar(@RequestBody MemberAvatarDeleteDTO memberAvatar) {
        memberAvatarAppService.deleteMemberAvatar(memberAvatar.getIds());
    }

}
