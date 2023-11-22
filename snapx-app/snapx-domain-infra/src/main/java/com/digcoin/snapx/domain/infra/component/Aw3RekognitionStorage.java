package com.digcoin.snapx.domain.infra.component;

import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.domain.infra.bo.ResourceFileBo;
import com.digcoin.snapx.domain.infra.constant.RekognitionConstant;
import com.digcoin.snapx.domain.infra.entity.ResourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.ModerationLabel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/18 10:39
 * @description
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Aw3RekognitionStorage implements RekognitionStorage {

    private final RekognitionClient rekognitionClient;

    @Override
    public void imageModeration(ResourceFileBo resourceFileBo) {
        ResourceFile resourceFile = resourceFileBo.getResourceFile();
        String fileName = resourceFile.getFileName();
        if (!fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
            return;
        }
        try (InputStream is = resourceFileBo.getFileSource().getInputStream()) {
            SdkBytes sourceBytes = SdkBytes.fromInputStream(is);
            Image souImage = Image.builder().bytes(sourceBytes).build();

            DetectModerationLabelsRequest moderationLabelsRequest = DetectModerationLabelsRequest.builder()
                    .image(souImage)
                    .minConfidence(60F)
                    .build();

            DetectModerationLabelsResponse moderationLabelsResponse = rekognitionClient.detectModerationLabels(moderationLabelsRequest);
            List<ModerationLabel> labels = moderationLabelsResponse.moderationLabels();
            if (CollectionUtils.isEmpty(labels)) {
                return;
            }
            List<ModerationLabel> moderationLabels = labels.stream().filter(label -> !RekognitionConstant.ALCOHOL.equals(label.parentName()) &&
                    !RekognitionConstant.TOBACCO.equals(label.parentName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(moderationLabels)) {
                String message = moderationLabels.stream().filter(label -> StringUtils.isNotBlank(label.parentName())).map(ModerationLabel::parentName).collect(Collectors.joining("、"));
                throw CommonError.UNEXPECT_ERROR.withMessage(String.format("The image you provided contains information such as %s discrimination.Please upload a new one. ", message));
            }
        } catch (IOException e) {
            log.error("createResourceFile resourceFile:[{}]", resourceFile, e);
            throw CommonError.UNEXPECT_ERROR.withCause(e);
        }

    }
}