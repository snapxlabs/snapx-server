package com.digcoin.snapx.server.admin.member.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.member.entity.MemberActivity;
import com.digcoin.snapx.domain.member.service.MemberActivityService;
import com.digcoin.snapx.server.admin.member.converter.AdminMemberActivityConverter;
import com.digcoin.snapx.server.admin.member.dto.AdminMemberActivityPageDTO;
import com.digcoin.snapx.server.admin.member.vo.AdminMemberActivityVO;
import com.digcoin.snapx.server.base.member.dto.BaseGiftCountDTO;
import com.digcoin.snapx.server.base.member.service.BaseGiftCountHandleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/6 14:33
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberActivityService {

    private final MemberActivityService memberActivityService;

    private final AdminMemberActivityConverter adminMemberActivityConverter;

    private final BaseGiftCountHandleService baseGiftCountHandleService;

    public PageResult<AdminMemberActivityVO> pageMemberActivity(AdminMemberActivityPageDTO dto) {
        PageResult<MemberActivity> pageResult = memberActivityService.pageMemberActivity(adminMemberActivityConverter.intoBO(dto));
        return PageResult.fromPageResult(pageResult, memberActivity -> {
            AdminMemberActivityVO vo = adminMemberActivityConverter.intoVO(memberActivity);
            BaseGiftCountDTO baseGiftCountDTO = baseGiftCountHandleService.buildActivityBaseGiftCountDTO(memberActivity.getId(), memberActivity.getMemberId());
            BeanUtils.copyProperties(baseGiftCountDTO, vo);
            return vo;
        });
    }
}
