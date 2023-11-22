package com.digcoin.snapx.domain.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.member.bo.InviterCommissionQuery;
import com.digcoin.snapx.domain.member.entity.InviterCommission;
import com.digcoin.snapx.domain.member.mapper.InviterCommissionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviterCommissionService {

    private final InviterCommissionMapper inviterCommissionMapper;

    public Long createInviterCommission(InviterCommission entity) {
        if (Objects.isNull(entity)) {
            return 0L;
        }
        inviterCommissionMapper.insert(entity);
        return entity.getId();
    }

    public PageResult<InviterCommission> pageInviterCommission(InviterCommissionQuery query) {
        IPage<InviterCommission> page = inviterCommissionMapper.selectPage(
                PageHelper.getPage(query),
                Wrappers.lambdaQuery(InviterCommission.class)
                        .eq(InviterCommission::getInviterMemberId, query.getInviterMemberId())
                        .eq(InviterCommission::getAccountType, query.getAccountType())
                        .orderByDesc(InviterCommission::getId));
        return PageResult.fromPage(page, Function.identity());
    }

}
