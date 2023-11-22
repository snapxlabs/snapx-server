package com.digcoin.snapx.server.app.member.converter.decorator;

import com.digcoin.snapx.domain.member.entity.Member;
import com.digcoin.snapx.server.app.member.converter.MemberConverter;
import com.digcoin.snapx.server.app.member.dto.MemberDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

public abstract class MemberConverterDecorator implements MemberConverter {

    @Autowired
    @Qualifier("delegate")
    private MemberConverter delegate;

    @Value("${app.member.defaults.avatar}")
    private String defaultAvatar;

    @Value("${app.member.defaults.avatar-cover}")
    private String defaultAvatarCover;

    @Override
    public Member fromDTO(MemberDTO member) {
        return delegate.fromDTO(member);
    }

    @Override
    public MemberDTO intoDTO(Member member) {
        MemberDTO dto = delegate.intoDTO(member);
        if (StringUtils.isBlank(dto.getAvatar())) {
            dto.setAvatar(defaultAvatar);
        }
        if (StringUtils.isBlank(dto.getAvatarCover())) {
            dto.setAvatarCover(defaultAvatarCover);
        }
        return dto;
    }

}
