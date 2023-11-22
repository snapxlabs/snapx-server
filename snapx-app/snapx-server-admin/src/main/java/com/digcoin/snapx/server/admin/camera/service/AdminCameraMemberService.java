package com.digcoin.snapx.server.admin.camera.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.camera.entity.CameraMember;
import com.digcoin.snapx.domain.camera.service.CameraMemberService;
import com.digcoin.snapx.server.admin.camera.converter.AdminCameraMemberConverter;
import com.digcoin.snapx.server.admin.camera.dto.AdminCameraMemberPageDTO;
import com.digcoin.snapx.server.admin.camera.vo.AdminCameraMemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/3/23 0:14
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCameraMemberService {

    private final CameraMemberService cameraMemberService;

    private final AdminCameraMemberConverter adminCameraMemberConverter;

    public PageResult<AdminCameraMemberVO> pageCameraMember(AdminCameraMemberPageDTO dto) {
        PageResult<CameraMember> pageResult = cameraMemberService.pageCameraMember(adminCameraMemberConverter.convertBO(dto));
        return PageResult.fromPageResult(pageResult, adminCameraMemberConverter::intoVO);
    }
}
