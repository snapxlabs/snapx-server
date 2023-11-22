package com.digcoin.snapx.server.app.member.converter;

import com.digcoin.snapx.domain.member.entity.MemberInteraction;
import com.digcoin.snapx.server.app.member.dto.AppMemberInteractionDTO;
import com.digcoin.snapx.server.app.member.vo.AppMemberInteractionVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 11:27
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppMemberInteractionConverter {

    AppMemberInteractionVO intoVO(MemberInteraction memberInteraction);

    MemberInteraction fromDTO(AppMemberInteractionDTO dto);

}
