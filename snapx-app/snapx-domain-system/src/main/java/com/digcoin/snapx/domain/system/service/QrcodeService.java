package com.digcoin.snapx.domain.system.service;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.enums.Operator;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.system.bo.QrcodeQuery;
import com.digcoin.snapx.domain.system.constant.QrcodeType;
import com.digcoin.snapx.domain.system.entity.Qrcode;
import com.digcoin.snapx.domain.system.mapper.QrcodeMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrcodeService {

    private final QrcodeMapper qrcodeMapper;
    private final IdentifierGenerator identifierGenerator;

    private static final Map<String, String> typeParamMap = Map.of(QrcodeType.INVITE, "inviteId");

    public Long createQrcode(Qrcode qrcode) {
        return createQrcode(qrcode, true);
    }

    public Long createQrcode(Qrcode qrcode, boolean genCodeBase64) {
        Objects.requireNonNull(qrcode.getMemberId());
        Objects.requireNonNull(qrcode.getCodeType());
        Objects.requireNonNull(qrcode.getWidth());
        Objects.requireNonNull(qrcode.getContent());

        qrcode.setId(identifierGenerator.nextId(qrcode).longValue());
        qrcode.setUsedCount(0);
        if (Objects.isNull(qrcode.getUseLimit())) {
            if (QrcodeType.INVITE.equals(qrcode.getCodeType())) {
                qrcode.setUseLimit(1);
            } else {
                qrcode.setUseLimit(0);
            }
        }
        
        String url = qrcode.getContent();
        if (StringUtils.isNotBlank(url) && typeParamMap.containsKey(qrcode.getCodeType())) {
            if (url.contains("#")) {
                String path = url.substring(url.lastIndexOf("#") + 1);
                String fragmentWithQueryParam = UriComponentsBuilder.fromPath(path)
                        .queryParam(typeParamMap.get(qrcode.getCodeType()), qrcode.getId())
                        .build()
                        .toString();
                String content = UriComponentsBuilder.fromUriString(url)
                        .fragment(fragmentWithQueryParam)
                        .build()
                        .toString();
                qrcode.setContent(content);
            } else {
                String content = UriComponentsBuilder.fromUriString(url)
                        .queryParam(typeParamMap.get(qrcode.getCodeType()), qrcode.getId())
                        .build()
                        .toString();
                qrcode.setContent(content);
            }
        }

        if (genCodeBase64) {
            String base64 = genQrcodeBase64(qrcode);
            qrcode.setCodeBase64(base64);
        }

        qrcodeMapper.insert(qrcode);

        return qrcode.getId();
    }

    public void updateQrcodeBase64(Qrcode qrcode) {
        String base64 = genQrcodeBase64(qrcode);
        qrcode.setCodeBase64(base64);
        qrcodeMapper.update(new Qrcode(),
                Wrappers.lambdaUpdate(Qrcode.class)
                        .set(Qrcode::getCodeBase64, base64)
                        .eq(Qrcode::getId, qrcode.getId()));
    }

    public String genQrcodeBase64(Qrcode qrcode) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrcode.getContent(), BarcodeFormat.QR_CODE, qrcode.getWidth(), qrcode.getWidth());

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, qrcode.getImageType(), pngOutputStream);
            byte[] bytes = pngOutputStream.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(bytes);

        } catch (WriterException e) {
            log.error("createQrcode zxing write exception qrocde:[{}]", qrcode, e);
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        } catch (IOException e) {
            log.error("createQrcode zxing io exception qrocde:[{}]", qrcode, e);
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }

    public Qrcode findQrcode(Long id) {
        return qrcodeMapper.selectById(id);
    }

    public byte[] decodeBase64(Qrcode qrcode) {
        if (Objects.isNull(qrcode) || StringUtils.isBlank(qrcode.getCodeBase64())) {
            return new byte[0];
        }

        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(qrcode.getCodeBase64());
    }

    public boolean updateQrcodeUsed(Qrcode qrcode) {
        if (Objects.isNull(qrcode)) {
            return false;
        }

        int useCount = qrcode.getUsedCount() + 1;
        if (qrcode.getUseLimit() > 0 && useCount > qrcode.getUseLimit()) {
            return false;
        }

        LocalDateTime writeOffTime = null;
        if (useCount == qrcode.getUseLimit()) {
            writeOffTime = LocalDateTime.now();
        }

        int update = qrcodeMapper.update(new Qrcode(), Wrappers.lambdaUpdate(Qrcode.class)
                .eq(Qrcode::getId, qrcode.getId())
                .eq(Qrcode::getUsedCount, qrcode.getUsedCount())
                .set(Qrcode::getUsedCount, useCount)
                .set(Qrcode::getWriteOffTime, writeOffTime));

        return update > 0;

    }

    public Qrcode getLatestUnusedQrCode(Long memberId, String codeType) {
        return Optional.ofNullable(qrcodeMapper.selectOne(Wrappers.lambdaQuery(Qrcode.class)
                        .eq(Qrcode::getMemberId, memberId)
                        .eq(Qrcode::getCodeType, codeType)
                        .isNull(Qrcode::getWriteOffTime)
                        .orderByDesc(Qrcode::getCreateTime)
                        .last("limit 1")))
                .orElse(null);
    }

    public PageResult<Qrcode> pageInviteCode(QrcodeQuery query) {
        return PageResult.fromPage(
                qrcodeMapper.selectPage(
                        PageHelper.getPage(query),
                        Wrappers.lambdaQuery(Qrcode.class)
                                .eq(Qrcode::getCodeType, query.getCodeType())
                                .eq(Qrcode::getOperator, Operator.ADMIN)
                                .orderByDesc(Qrcode::getCreateTime)),
                Function.identity());
    }

}
