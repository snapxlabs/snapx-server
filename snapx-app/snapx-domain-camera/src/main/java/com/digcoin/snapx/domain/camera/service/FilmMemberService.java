package com.digcoin.snapx.domain.camera.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.constant.Common;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.camera.bo.FilmMemberDetailQueryBO;
import com.digcoin.snapx.domain.camera.bo.FilmMemberQueryBO;
import com.digcoin.snapx.domain.camera.entity.FilmMember;
import com.digcoin.snapx.domain.camera.entity.FilmMemberDetail;
import com.digcoin.snapx.domain.camera.enums.FilmChangeType;
import com.digcoin.snapx.domain.camera.error.FilmMemberError;
import com.digcoin.snapx.domain.camera.mapper.FilmMemberDetailMapper;
import com.digcoin.snapx.domain.camera.mapper.FilmMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 14:38
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FilmMemberService {

    private final FilmMemberMapper filmMemberMapper;

    private final FilmMemberDetailMapper filmMemberDetailMapper;

    public FilmMember findFilmMemberByMemberId(Long memberId) {
        return filmMemberMapper.selectOne(Wrappers.<FilmMember>lambdaQuery().eq(FilmMember::getMemberId, memberId));
    }

    public List<FilmMemberDetail> findFilmMemberDetailByMemberId(Long memberId) {
        return filmMemberDetailMapper.selectList(Wrappers.<FilmMemberDetail>lambdaQuery()
                .eq(FilmMemberDetail::getMemberId, memberId)
                .orderByDesc(FilmMemberDetail::getCreateTime));
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateFilmMember(Long memberId, Long variableQuantity, FilmChangeType filmChangeType) {
        saveFilmMemberDetail(memberId, variableQuantity, filmChangeType);
        FilmMember filmMember = this.findFilmMemberByMemberId(memberId);
        if (Objects.isNull(filmMember)) {
            if (variableQuantity < 0) {
                throw FilmMemberError.FILM_BALANCE_INSUFFICIENT.withDefaults();
            }
            filmMember = new FilmMember();
            filmMember.setMemberId(memberId);
            filmMember.setRemainingQuantity(variableQuantity);
            filmMemberMapper.insert(filmMember);
            return;
        }
        long remainingQuantity = filmMember.getRemainingQuantity() + variableQuantity;
        if (remainingQuantity < 0) {
            throw FilmMemberError.FILM_BALANCE_INSUFFICIENT.withDefaults();
        }
        int update = filmMemberMapper.update(null, Wrappers.<FilmMember>lambdaUpdate()
                .eq(FilmMember::getId, filmMember.getId())
                .eq(FilmMember::getRemainingQuantity, filmMember.getRemainingQuantity())
                .set(FilmMember::getRemainingQuantity, remainingQuantity));
        if (update == 0) {
            throw FilmMemberError.FILM_BALANCE_UPDATE_FAILED.withDefaults();
        }
    }

    private void saveFilmMemberDetail(Long memberId, Long variableQuantity, FilmChangeType filmChangeType) {
        FilmMemberDetail filmMemberDetail = new FilmMemberDetail();
        filmMemberDetail.setMemberId(memberId);
        filmMemberDetail.setFilmChangeType(filmChangeType);
        filmMemberDetail.setVariableQuantity(variableQuantity);
        filmMemberDetailMapper.insert(filmMemberDetail);
    }

    public Long getTotalQuantity(Long memberId) {
        return filmMemberDetailMapper.getTotalQuantity(memberId);
    }

    public Long getFilmMemberDetailCount(Long memberId, FilmChangeType filmChangeType) {
        return filmMemberDetailMapper.selectCount(Wrappers.<FilmMemberDetail>lambdaQuery()
                .eq(FilmMemberDetail::getMemberId, memberId)
                .eq(FilmMemberDetail::getFilmChangeType, filmChangeType));
    }

    public PageResult<FilmMember> pageFilmMember(FilmMemberQueryBO queryBO) {
        QueryWrapper<FilmMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cfm.deleted", Common.ZERO)
                .like(StringUtils.isNotBlank(queryBO.getMemberNickName()), "mm.nickname", queryBO.getMemberNickName())
                .like(StringUtils.isNotBlank(queryBO.getMemberAccount()), "mm.account", queryBO.getMemberAccount())
                .last("order by cfm.create_time desc ");
        return PageResult.fromPage(filmMemberMapper.pageFilmMember(PageHelper.getPage(queryBO), queryWrapper), Function.identity());
    }

    public PageResult<FilmMemberDetail> pageFilmMemberDetail(FilmMemberDetailQueryBO queryBO) {
        return PageResult.fromPage(filmMemberDetailMapper.selectPage(PageHelper.getPage(queryBO),
                Wrappers.<FilmMemberDetail>lambdaQuery().eq(FilmMemberDetail::getMemberId, queryBO.getMemberId())
        ), Function.identity());
    }
}
