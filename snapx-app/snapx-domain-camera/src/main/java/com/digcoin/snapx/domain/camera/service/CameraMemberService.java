package com.digcoin.snapx.domain.camera.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.constant.Common;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.camera.bo.CameraMemberQueryBO;
import com.digcoin.snapx.domain.camera.entity.CameraMember;
import com.digcoin.snapx.domain.camera.mapper.CameraMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/22 15:27
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CameraMemberService {

    private final CameraMemberMapper cameraMemberMapper;

    public List<CameraMember> listCameraMember(Long memberId) {
        return cameraMemberMapper.selectList(Wrappers.<CameraMember>lambdaQuery()
                .eq(CameraMember::getMemberId, memberId).orderByDesc(CameraMember::getCreateTime));
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSaveCameraMember(List<CameraMember> cameraMembers) {
        for(CameraMember cameraMember : cameraMembers){
            cameraMemberMapper.insert(cameraMember);
        }
    }

    public PageResult<CameraMember> pageCameraMember(CameraMemberQueryBO queryBO) {
        QueryWrapper<CameraMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ccm.deleted", Common.ZERO)
                .like(StringUtils.isNotBlank(queryBO.getMemberNickName()), "mm.nickname", queryBO.getMemberNickName())
                .like(StringUtils.isNotBlank(queryBO.getMemberAccount()), "mm.account", queryBO.getMemberAccount())
                .like(StringUtils.isNotBlank(queryBO.getCameraName()), "cc.name", queryBO.getCameraName())
                .like(StringUtils.isNotBlank(queryBO.getCameraCode()), "cc.code", queryBO.getCameraCode())
                .last("order by ccm.create_time desc ");
        return PageResult.fromPage(cameraMemberMapper.pageCameraMember(PageHelper.getPage(queryBO), queryWrapper), Function.identity());
    }
}
