package com.digcoin.snapx.domain.member.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.domain.member.entity.MemberDiscordBinding;
import com.digcoin.snapx.domain.member.mapper.MemberDiscordBindingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDiscordBindingService {

    private final MemberDiscordBindingMapper memberDiscordBindingMapper;

    public void createMemberDiscordBinding(Long memberId, String discordUserId) {
        MemberDiscordBinding exist = memberDiscordBindingMapper.selectOne(Wrappers.lambdaQuery(MemberDiscordBinding.class)
                        .eq(MemberDiscordBinding::getMemberId, memberId)
                        .eq(MemberDiscordBinding::getDiscordUserId, discordUserId));
        if (Objects.nonNull(exist)) {
            return;
        }
        Long count = memberDiscordBindingMapper.selectCount(Wrappers.lambdaQuery(MemberDiscordBinding.class)
                .eq(MemberDiscordBinding::getMemberId, memberId)
                .or()
                .eq(MemberDiscordBinding::getDiscordUserId, discordUserId)
        );
        if (count > 0) {
            log.warn("createMemberDiscordBinding fail memberId, discordUserId", memberId, discordUserId);
            throw CommonError.UNEXPECT_ERROR.withMessage("Discord User had bound with other member");
        }

        MemberDiscordBinding binding = new MemberDiscordBinding();
        binding.setMemberId(memberId);
        binding.setDiscordUserId(discordUserId);
        memberDiscordBindingMapper.insert(binding);
    }

    public MemberDiscordBinding findMemberDiscordBinding(Long memberId) {
        return memberDiscordBindingMapper.selectOne(
                Wrappers.lambdaQuery(MemberDiscordBinding.class)
                        .eq(MemberDiscordBinding::getMemberId, memberId));
    }

}
