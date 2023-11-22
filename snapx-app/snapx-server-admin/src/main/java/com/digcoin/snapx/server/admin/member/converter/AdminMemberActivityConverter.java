package com.digcoin.snapx.server.admin.member.converter;

import com.digcoin.snapx.domain.member.bo.MemberActivityQueryBO;
import com.digcoin.snapx.domain.member.entity.MemberActivity;
import com.digcoin.snapx.server.admin.member.dto.AdminMemberActivityPageDTO;
import com.digcoin.snapx.server.admin.member.vo.AdminMemberActivityVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/6 15:03
 * @description
 */
@Mapper(componentModel = "spring")
public interface AdminMemberActivityConverter {

    MemberActivityQueryBO intoBO(AdminMemberActivityPageDTO dto);

    AdminMemberActivityVO intoVO(MemberActivity memberActivity);
}
