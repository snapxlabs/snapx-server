package com.digcoin.snapx.server.app.member.service;

import com.digcoin.snapx.domain.infra.bo.ExchangeTokenBO;
import com.digcoin.snapx.domain.infra.component.discord.model.Guild;
import com.digcoin.snapx.domain.infra.component.discord.model.TokensResponse;
import com.digcoin.snapx.domain.infra.config.properties.DiscordOauthProperties;
import com.digcoin.snapx.domain.infra.service.DiscordService;
import com.digcoin.snapx.domain.member.entity.MemberDiscordBinding;
import com.digcoin.snapx.domain.member.service.MemberDiscordBindingService;
import com.digcoin.snapx.domain.member.service.MemberInteractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDiscordAppService {

    private final DiscordService discordService;
    private final MemberDiscordBindingService memberDiscordBindingService;
    private final MemberInteractionService memberInteractionService;
    private final AppMemberInteractionService appMemberInteractionService;
    private final DiscordOauthProperties discordOauthProperties;

    public String getUrl(String state) {
        return discordService.getUrl(state);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bind(String code, Long memberId) {
        ExchangeTokenBO exchangeTokenBO = discordService.exchangeToken(code);

        String discordUserId = exchangeTokenBO.getUser().getId();
        String officialGuildId = discordOauthProperties.getOfficialGuildId();
        discordService.addGuildMember(exchangeTokenBO.getTokens(), officialGuildId, discordUserId);

        memberDiscordBindingService.createMemberDiscordBinding(memberId, discordUserId);
        memberInteractionService.updateJoinDiscord(memberId);
        appMemberInteractionService.distributingGifts(memberId);
    }

    public boolean isMemberInOfficialGuild(Long memberId) {
        String officialGuildId = discordOauthProperties.getOfficialGuildId();
        MemberDiscordBinding binding = memberDiscordBindingService.findMemberDiscordBinding(memberId);
        if (Objects.isNull(binding)) {
            return false;
        }
        TokensResponse tokens = discordService.getTokens(binding.getDiscordUserId());
        if (Objects.isNull(tokens)) {
            return false;
        }
        List<Guild> guildList = discordService.listGuild(tokens);
        if (CollectionUtils.isEmpty(guildList)) {
            return false;
        }
        return guildList.stream()
                .map(Guild::getId)
                .collect(Collectors.toSet())
                .contains(officialGuildId);
    }

}
