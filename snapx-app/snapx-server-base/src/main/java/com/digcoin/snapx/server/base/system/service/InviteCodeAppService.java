package com.digcoin.snapx.server.base.system.service;

import com.digcoin.snapx.core.common.enums.Operator;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.system.bo.QrcodeQuery;
import com.digcoin.snapx.domain.system.constant.QrcodeType;
import com.digcoin.snapx.domain.system.entity.Qrcode;
import com.digcoin.snapx.domain.system.service.QrcodeService;
import com.digcoin.snapx.server.base.system.config.QrcodeProperties;
import com.digcoin.snapx.server.base.system.converter.QrcodeConverter;
import com.digcoin.snapx.server.base.system.dto.QrcodeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteCodeAppService {

    private final QrcodeService qrcodeService;
    private final QrcodeProperties qrcodeProperties;
    private final QrcodeConverter qrcodeConverter;

    public QrcodeDTO createInviteCode(Operator operator, Long operatorId) {
        Long id = doCreateInviteCode(operator, operatorId);
        return findInviteCode(id);
    }

    public QrcodeDTO getLatestUnusedInviteCode(Long memberId) {
        Qrcode qrCode = qrcodeService.getLatestUnusedQrCode(memberId, QrcodeType.INVITE);
        return qrcodeConverter.intoDTO(qrCode);
    }

    public QrcodeDTO findInviteCode(Long qrcodeId) {
        Qrcode qrcode = qrcodeService.findQrcode(qrcodeId);
        return qrcodeConverter.intoDTO(qrcode);
    }

    public PageResult<QrcodeDTO> pageInviteCode(QrcodeQuery query) {
        query.setCodeType(QrcodeType.INVITE);
        return PageResult.fromPageResult(qrcodeService.pageInviteCode(query), qrcodeConverter::intoDTO);
    }

    public void bathCreateInviteCode(Operator operator, Long operatorId, Integer num, Integer useLimit) {
        if (num > 100) {
            throw CommonError.PARAMETER_ERROR.withMessage("Invite Code Num can not over 100");
        }
        for (Integer i = 0; i < num; i++) {
            doCreateInviteCode(operator, operatorId, useLimit);
        }
    }

    private Long doCreateInviteCode(Operator operator, Long operatorId) {
        return doCreateInviteCode(operator, operatorId, null);
    }

    private Long doCreateInviteCode(Operator operator, Long operatorId, Integer useLimit) {
        QrcodeProperties.CodeDescription inviteProp = qrcodeProperties.getInvite();
        Qrcode entity = new Qrcode();
        entity.setMemberId(operatorId);
        entity.setOperator(operator);
        entity.setUseLimit(useLimit);
        entity.setCodeType(QrcodeType.INVITE);
        entity.setContent(inviteProp.getUrl());
        entity.setWidth(inviteProp.getWidth());
        entity.setImageType(inviteProp.getImageType());
        return qrcodeService.createQrcode(entity, false);
    }

}
