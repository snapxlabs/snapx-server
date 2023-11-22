package com.digcoin.snapx.server.app.member.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.digcoin.snapx.core.advice.IgnoreApiBody;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.server.app.member.dto.*;
import com.digcoin.snapx.server.app.member.service.MemberAppService;
import com.digcoin.snapx.server.base.infra.component.CaptchaChannel;
import com.digcoin.snapx.server.base.infra.component.CaptchaType;
import com.digcoin.snapx.server.base.member.dto.MemberAvatarDTO;
import com.digcoin.snapx.server.base.member.service.MemberAvatarAppService;
import com.digcoin.snapx.server.base.system.dto.QrcodeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Tag(name = "102 - 会员 - 个人信息")
@Slf4j
@SaCheckLogin
@Validated
@RestController
@RequestMapping("/member/profile")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberAppService memberAppService;
    private final MemberAvatarAppService memberAvatarAppService;

    @Operation(summary = "获取当前会员信息")
    @GetMapping("find-current-member")
    public MemberDTO findCurrentMember(@Schema(hidden = true) CurrentUser currentUser) {
        return memberAppService.findMember(currentUser.getId());
    }

    @Operation(summary = "修改当前会员登录密码")
    @PostMapping("update-member-password")
    public AuthDTO updateMemberPassword(@Schema(hidden = true) CurrentUser currentUser,
                                        @RequestBody MemberPasswordDTO memberPasswordDTO) {
        memberPasswordDTO.setMemberId(currentUser.getId());
        memberPasswordDTO.setAccount(currentUser.getUsername());
        memberPasswordDTO.setCaptchaType(CaptchaType.MODIFY_PASSWORD);
        return memberAppService.updateMemberPassword(memberPasswordDTO);
    }

    @Operation(summary = "修改当前会员个人信息")
    @PostMapping("update-member-profile")
    public MemberDTO updateMemberProfile(@Schema(hidden = true) CurrentUser currentUser,
                                         @RequestBody MemberProfileDTO memberProfileDTO) {
        memberProfileDTO.setMemberId(currentUser.getId());
        return memberAppService.updateMemberProfile(memberProfileDTO);
    }

    @Operation(summary = "绑定手机号码")
    @PostMapping("bind-phone")
    public void bindPhone(@Schema(hidden = true) CurrentUser currentUser,
                                  @RequestBody MemberPhoneDTO memberPhoneDTO) {
        memberPhoneDTO.setMemberId(currentUser.getId());
        memberPhoneDTO.setCaptchaChannel(CaptchaChannel.CONST);
        memberAppService.bindPhone(memberPhoneDTO);
    }

    @Operation(summary = "生成邀请码")
    @PostMapping("create-invite-qrcode")
    public QrcodeDTO createInviteQrcode(@Schema(hidden = true) CurrentUser currentUser) {
        return memberAppService.createInviteQrcode(currentUser.getId());
    }

    @Operation(summary = "分页获取邀请用户列表")
    @PostMapping("page-invitee-member")
    public MemberInvitationDTO pageInviteeMember(@Schema(hidden = true) CurrentUser currentUser, MemberInvitationQuery query) {
        query.setInviterMemberId(currentUser.getId());
        return memberAppService.pageInviteeMember(query);
    }

    @SaIgnore
    @Operation(summary = "生成分享码")
    @PostMapping("create-share-qrcode")
    public QrcodeDTO createShareQrcode() {
        return memberAppService.createShareQrcode();
    }

    @SaIgnore
    @Operation(summary = "获取生成邀请码详情图片")
    @GetMapping("get-invite-qrcode/{qrcodeId}")
    @IgnoreApiBody
    public void getInviteQrcode(@Schema(description = "二维码id") @PathVariable Long qrcodeId,
                                HttpServletResponse response) {
        byte[] bytes = memberAppService.getInviteQrcode(qrcodeId);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(bytes);
        } catch (IOException e) {
            log.error("getInviteQrcode response error", e);
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }

    @SaIgnore
    @Operation(summary = "获取用户备选头像列表")
    @GetMapping("list-member-avatar")
    public List<MemberAvatarDTO> listMemberAvatar() {
        return memberAvatarAppService.listMemberAvatar();
    }

}
