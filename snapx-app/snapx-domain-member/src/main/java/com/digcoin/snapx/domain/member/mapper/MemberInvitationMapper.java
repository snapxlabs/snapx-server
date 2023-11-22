package com.digcoin.snapx.domain.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digcoin.snapx.domain.member.bo.InviteeCountMap;
import com.digcoin.snapx.domain.member.entity.MemberInvitation;

import java.util.Collection;
import java.util.List;

public interface MemberInvitationMapper extends BaseMapper<MemberInvitation> {

    List<InviteeCountMap> getInviteeCountMap(Collection<Long> inviterMemberIds);

}
