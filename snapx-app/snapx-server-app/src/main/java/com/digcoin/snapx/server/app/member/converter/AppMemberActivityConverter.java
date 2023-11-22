package com.digcoin.snapx.server.app.member.converter;

import com.digcoin.snapx.domain.member.entity.MemberActivity;
import com.digcoin.snapx.server.app.member.vo.AppMemberActivityVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/5 17:25
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppMemberActivityConverter {

    AppMemberActivityVO intoVO(MemberActivity memberActivity);

}
