package com.digcoin.snapx.domain.system.service;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.system.bo.ActivityQueryBO;
import com.digcoin.snapx.domain.system.entity.Activity;
import com.digcoin.snapx.domain.system.mapper.ActivityMapper;
import com.digcoin.snapx.domain.system.utils.QrCodeSvgUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/1/26 17:30
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final String prefix = "activityId://";

    private final ActivityMapper activityMapper;

    private final IdentifierGenerator identifierGenerator;

    public Activity findById(Long id) {
        return activityMapper.selectById(id);
    }

    public List<Activity> findAll() {
        return activityMapper.selectList(Wrappers.<Activity>lambdaQuery().orderByDesc(Activity::getCreateTime));
    }

    public List<Activity> findActivityList(Set<Long> activityIds) {
        if (activityIds.isEmpty()) {
            return Collections.emptyList();
        }
        return activityMapper.selectList(Wrappers.<Activity>lambdaQuery().in(Activity::getId, activityIds));
    }

    public PageResult<Activity> pageActivity(ActivityQueryBO activityQueryBO) {
        return PageResult.fromPage(activityMapper.selectPage(PageHelper.getPage(activityQueryBO),
                Wrappers.<Activity>lambdaQuery()
                        .like(StringUtils.isNotBlank(activityQueryBO.getName()), Activity::getName, activityQueryBO.getName())
                        .eq(Objects.nonNull(activityQueryBO.getIsSpec()), Activity::getIsSpec, activityQueryBO.getIsSpec())
                        .orderByDesc(Activity::getCreateTime)), Function.identity());
    }

    public void updateActivity(Activity activity) {
        activity.setUpdateTime(LocalDateTime.now());
        activityMapper.updateById(activity);
    }

    public void deleteActivity(Activity activity) {
        activityMapper.deleteById(activity);
    }

    public void createActivity(Activity activity) {
        Number number = identifierGenerator.nextId(activity);
        activity.setId(number.longValue());
        String urlContent = String.join("", prefix, number.toString());
        activity.setQrCodeBase64(genQrcodeBase64(urlContent));
        activityMapper.insert(activity);
    }


    public String genQrcodeBase64(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content,
                    BarcodeFormat.QR_CODE, 500, 500);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] bytes = pngOutputStream.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(bytes);

        } catch (WriterException e) {
            log.error("createQrcode zxing write exception content:[{}]", content, e);
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        } catch (IOException e) {
            log.error("createQrcode zxing io exception content:[{}]", content, e);
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }

    public String getActivitySvg(Long id) {
        String urlContent = String.join("", prefix, id.toString());
        return QrCodeSvgUtil.getQrCodeSvg(urlContent);
    }
}



