package com.digcoin.snapx.server.app.member.service;

import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import com.digcoin.snapx.domain.camera.service.FilmMemberService;
import com.digcoin.snapx.domain.infra.service.DiscordService;
import com.digcoin.snapx.domain.member.entity.MemberInteraction;
import com.digcoin.snapx.domain.member.service.MemberInteractionService;
import com.digcoin.snapx.domain.system.entity.SystemSetting;
import com.digcoin.snapx.domain.system.service.SystemSettingService;
import com.digcoin.snapx.server.app.member.converter.AppMemberInteractionConverter;
import com.digcoin.snapx.server.app.member.dto.AppMemberInteractionDTO;
import com.digcoin.snapx.server.app.member.vo.AppMemberInteractionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 11:23
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppMemberInteractionService {

    private final MemberInteractionService memberInteractionService;

    private final SystemSettingService systemSettingService;

    private final FilmMemberService filmMemberService;

    private final AppMemberInteractionConverter appMemberInteractionConverter;
    private final DiscordService discordService;

    public AppMemberInteractionVO getMemberInteraction(Long memberId) {
        String discordUrl = discordService.getUrl(String.valueOf(memberId));
        SystemSetting systemSetting = systemSettingService.findSystemSetting();
        Optional<MemberInteraction> optional = Optional.ofNullable(memberInteractionService.findMemberInteractionByMemberId(memberId));
        if (optional.isEmpty()) {
            AppMemberInteractionVO appMemberInteractionVO = new AppMemberInteractionVO();
            appMemberInteractionVO.setMemberId(memberId);
            appMemberInteractionVO.setIsFollowTwitter(Boolean.FALSE);
            appMemberInteractionVO.setIsJoinDiscord(Boolean.FALSE);
            appMemberInteractionVO.setFollowGiftCount(systemSetting.getFollowGiftCount());
            appMemberInteractionVO.setDiscordUrl(discordUrl);
            return appMemberInteractionVO;
        }
        return optional.map(m -> {
            AppMemberInteractionVO appMemberInteractionVO = appMemberInteractionConverter.intoVO(m);
            appMemberInteractionVO.setFollowGiftCount(systemSetting.getFollowGiftCount());
            appMemberInteractionVO.setDiscordUrl(discordUrl);
            return appMemberInteractionVO;
        }).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public AppMemberInteractionVO editMemberInteraction(AppMemberInteractionDTO dto) {
        if (Boolean.TRUE.equals(dto.getIsJoinDiscord())) {
            memberInteractionService.updateJoinDiscord(dto.getMemberId());
        }
        if (Boolean.TRUE.equals(dto.getIsFollowTwitter())) {
            memberInteractionService.updateFollowTwitter(dto.getMemberId());
        }
        distributingGifts(dto.getMemberId());
        return getMemberInteraction(dto.getMemberId());
    }

    public void distributingGifts(Long memberId) {
        SystemSetting systemSetting = systemSettingService.findSystemSetting();
        if(Boolean.FALSE.equals(systemSetting.getIsFollowGift())){
            return ;
        }

        MemberInteraction memberInteraction = memberInteractionService.findMemberInteractionByMemberId(memberId);
        if (Objects.isNull(memberInteraction)) {
            return;
        }

        if (Boolean.TRUE.equals(memberInteraction.getIsJoinDiscord())
                && Boolean.TRUE.equals(memberInteraction.getIsFollowTwitter())) {
            if (filmMemberService.getFilmMemberDetailCount(memberId, FilmChangeType.INTERACTION_GIFT) == 0) {
                filmMemberService.saveOrUpdateFilmMember(memberId, systemSetting.getFollowGiftCount(), FilmChangeType.INTERACTION_GIFT);
            }
        }
    }
}
