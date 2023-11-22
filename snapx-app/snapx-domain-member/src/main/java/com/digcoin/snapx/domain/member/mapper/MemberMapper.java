package com.digcoin.snapx.domain.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digcoin.snapx.domain.member.bo.InviteeCountMap;
import com.digcoin.snapx.domain.member.entity.Member;

import java.util.Collection;
import java.util.List;

public interface MemberMapper extends BaseMapper<Member> {

    Long getTotalSteakCount();

}
