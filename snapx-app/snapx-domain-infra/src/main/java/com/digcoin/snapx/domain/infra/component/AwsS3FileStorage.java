package com.digcoin.snapx.domain.infra.component;

import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.domain.infra.bo.ResourceFileBo;
import com.digcoin.snapx.domain.infra.config.ResourceFileProperties;
import com.digcoin.snapx.domain.infra.entity.ResourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@ConditionalOnProperty(name = "app.resource-file.strategy", havingValue = "s3")
@Component
@RequiredArgsConstructor
public class AwsS3FileStorage extends AbstractFileStorage implements FileStorage {

    private final ResourceFileProperties resourceFileProperties;
    private final S3Client s3Client;

    @Override
    public void save(ResourceFileBo resourceFileBo) {

        ResourceFile resourceFile = resourceFileBo.getResourceFile();
        String bucket = getBucket();
        String key = getFilePath(resourceFile);
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .contentType(resourceFile.getContentType())
                .key(key)
                .build();

        try (InputStream is = resourceFileBo.getFileSource().getInputStream()) {

            s3Client.putObject(objectRequest, RequestBody.fromInputStream(is, resourceFile.getFileSize()));

        } catch (FileNotFoundException e) {
            log.error("createResourceFile resourceFile:[{}]", resourceFile, e);
            throw CommonError.UNEXPECT_ERROR.withCause(e);

        } catch (IOException e) {
            log.error("createResourceFile resourceFile:[{}]", resourceFile, e);
            throw CommonError.UNEXPECT_ERROR.withCause(e);
        }

    }

    @Override
    public ResourceFileBo load(ResourceFile resourceFile) {
        String fileUrl = getFileUrl(resourceFile);
        String key = getFilePath(resourceFile);
        return ResourceFileBo.fromEntity(resourceFile, fileUrl, new FileSource(key));
    }

    @Override
    protected String geSeparator() {
        return "/";
    }

    private String getBucket() {
        return resourceFileProperties.getS3().getBucket();
    }

    private String getFileUrl(ResourceFile resourceFile) {
        return resourceFileProperties.getS3().getCdnDomain() + "/" + getFilePath(resourceFile);
    }

    private class FileSource implements InputStreamSource {

        private final String key;

        public FileSource(String key) {
            this.key = key;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            String bucket = getBucket();
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            return s3Client.getObject(getObjectRequest);
        }
    }
}
