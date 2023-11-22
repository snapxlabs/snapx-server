package com.digcoin.snapx.server.app.camera.converter;

import com.digcoin.snapx.domain.camera.entity.FilmMember;
import com.digcoin.snapx.domain.camera.entity.FilmMemberDetail;
import com.digcoin.snapx.server.app.camera.vo.AppFilmMemberVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 17:09
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppFilmMemberConverter {

    AppFilmMemberVO intoVO(FilmMember filmMember);

    AppFilmMemberVO.AppFilmMemberDetailVO intoVO (FilmMemberDetail filmMemberDetail);
}
