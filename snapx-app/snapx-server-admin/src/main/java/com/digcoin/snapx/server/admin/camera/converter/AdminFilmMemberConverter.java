package com.digcoin.snapx.server.admin.camera.converter;

import com.digcoin.snapx.domain.camera.bo.FilmMemberDetailQueryBO;
import com.digcoin.snapx.domain.camera.bo.FilmMemberQueryBO;
import com.digcoin.snapx.domain.camera.entity.FilmMember;
import com.digcoin.snapx.domain.camera.entity.FilmMemberDetail;
import com.digcoin.snapx.server.admin.camera.dto.AdminFilmMemberDetailPageDTO;
import com.digcoin.snapx.server.admin.camera.dto.AdminFilmMemberPageDTO;
import com.digcoin.snapx.server.admin.camera.vo.AdminFilmMemberDetailVO;
import com.digcoin.snapx.server.admin.camera.vo.AdminFilmMemberVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/23 0:45
 * @description
 */
@Mapper(componentModel = "spring")
public interface AdminFilmMemberConverter {

    FilmMemberQueryBO convertBO(AdminFilmMemberPageDTO dto);

    AdminFilmMemberVO intoVO(FilmMember filmMember);

    FilmMemberDetailQueryBO convertBO(AdminFilmMemberDetailPageDTO dto);

    AdminFilmMemberDetailVO intoVO(FilmMemberDetail filmMemberDetail);
}
