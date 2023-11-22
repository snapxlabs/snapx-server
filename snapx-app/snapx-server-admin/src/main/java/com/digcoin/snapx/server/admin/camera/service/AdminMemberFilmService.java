package com.digcoin.snapx.server.admin.camera.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.camera.entity.FilmMember;
import com.digcoin.snapx.domain.camera.entity.FilmMemberDetail;
import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import com.digcoin.snapx.domain.camera.service.FilmMemberService;
import com.digcoin.snapx.server.admin.camera.converter.AdminFilmMemberConverter;
import com.digcoin.snapx.server.admin.camera.dto.AdminFilmMemberAdjustDTO;
import com.digcoin.snapx.server.admin.camera.dto.AdminFilmMemberDetailPageDTO;
import com.digcoin.snapx.server.admin.camera.dto.AdminFilmMemberPageDTO;
import com.digcoin.snapx.server.admin.camera.vo.AdminFilmMemberDetailVO;
import com.digcoin.snapx.server.admin.camera.vo.AdminFilmMemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/22 1:17
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberFilmService {

    private final AdminFilmMemberConverter adminFilmMemberConverter;

    private final FilmMemberService filmMemberService;

    public PageResult<AdminFilmMemberVO> pageFilmMember(AdminFilmMemberPageDTO dto) {
        PageResult<FilmMember> pageResult = filmMemberService.pageFilmMember(adminFilmMemberConverter.convertBO(dto));
        return PageResult.fromPageResult(pageResult, filmMember -> {
            AdminFilmMemberVO vo = adminFilmMemberConverter.intoVO(filmMember);
            vo.setTotalQuantity(filmMemberService.getTotalQuantity(filmMember.getMemberId()));
            return vo;
        });
    }

    public PageResult<AdminFilmMemberDetailVO> pageFilmMemberDetail(AdminFilmMemberDetailPageDTO dto) {
        PageResult<FilmMemberDetail> pageResult = filmMemberService.pageFilmMemberDetail(
                adminFilmMemberConverter.convertBO(dto));
        return PageResult.fromPageResult(pageResult, adminFilmMemberConverter::intoVO);
    }

    public void adjustFilmMemberQuantity(AdminFilmMemberAdjustDTO dto) {
        filmMemberService.saveOrUpdateFilmMember(dto.getMemberId(), dto.getAdjustQuantity(), FilmChangeType.ADMIN_ADJUST);

    }
}
