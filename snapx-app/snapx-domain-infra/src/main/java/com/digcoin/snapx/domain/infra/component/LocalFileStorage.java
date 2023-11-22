package com.digcoin.snapx.domain.infra.component;

import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.domain.infra.bo.ResourceFileBo;
import com.digcoin.snapx.domain.infra.config.ResourceFileProperties;
import com.digcoin.snapx.domain.infra.entity.ResourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@ConditionalOnProperty(name = "app.resource-file.strategy", havingValue = "local")
@Component
@RequiredArgsConstructor
public class LocalFileStorage extends AbstractFileStorage implements FileStorage {

    private final ResourceFileProperties resourceFileProperties;

    @Override
    public void save(ResourceFileBo resourceFileBo) {
        ResourceFile resourceFile = resourceFileBo.getResourceFile();
        File file = getFile(resourceFile);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try (OutputStream os = new FileOutputStream(file);
             InputStream is = resourceFileBo.getFileSource().getInputStream()) {

            IOUtils.copy(is, os);

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
        File file = getFile(resourceFile);
        return ResourceFileBo.fromEntity(resourceFile, fileUrl, new FileSource(file));
    }

    @Override
    protected String geSeparator() {
        return File.separator;
    }

    private String getFileUrl(ResourceFile resourceFile) {
        return resourceFileProperties.getResourceUrl() + resourceFileProperties.getPath() + "/" + getFilePath(resourceFile);
    }

    private File getFile(ResourceFile resourceFile) {
        return new File(getFileLocation(resourceFile));
    }

    private String getFileLocation(ResourceFile resourceFile) {
        String location = resourceFileProperties.getLocation();
        return String.join(File.separator, location, getFilePath(resourceFile));
    }

    private class FileSource implements InputStreamSource {

        private final File file;

        public FileSource(File file) {
            this.file = file;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(this.file);
        }
    }
}
