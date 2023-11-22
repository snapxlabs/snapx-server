package com.digcoin.snapx.server.app.camera.service;

import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import com.digcoin.snapx.domain.camera.service.FilmMemberService;
import com.digcoin.snapx.server.app.camera.converter.AppFilmMemberConverter;
import com.digcoin.snapx.server.app.camera.dto.FilmMemberQuantityReduceDTO;
import com.digcoin.snapx.server.app.camera.vo.AppFilmMemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 14:31
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppFilmMemberService {

    private final FilmMemberService filmMemberService;

    private final AppFilmMemberConverter appFilmMemberConverter;

    public AppFilmMemberVO getFilmMember(Long memberId) {
        AppFilmMemberVO empty = new AppFilmMemberVO();
        empty.setMemberId(memberId);
        empty.setTotalQuantity(100L);
        empty.setRemainingQuantity(0L);
        empty.setDetailVOList(Collections.emptyList());
        return Optional.ofNullable(filmMemberService.findFilmMemberByMemberId(memberId)).map(filmMember -> {
            AppFilmMemberVO appFilmMemberVO = appFilmMemberConverter.intoVO(filmMember);
            appFilmMemberVO.setDetailVOList(filmMemberService.findFilmMemberDetailByMemberId(memberId)
                    .stream().map(appFilmMemberConverter::intoVO).collect(Collectors.toList()));
//            appFilmMemberVO.setTotalQuantity(filmMemberService.getTotalQuantity(memberId));
            appFilmMemberVO.setTotalQuantity(100L);
            return appFilmMemberVO;
        }).orElse(empty);
    }

    public AppFilmMemberVO reduceFilmMemberQuantity(FilmMemberQuantityReduceDTO dto) {
        filmMemberService.saveOrUpdateFilmMember(dto.getMemberId(), dto.getChangeQuantity() * -1L, FilmChangeType.PHOTOGRAPH);
        return getFilmMember(dto.getMemberId());
    }
}
