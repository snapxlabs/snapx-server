package com.digcoin.snapx.domain.infra.bo;

import com.digcoin.snapx.domain.infra.entity.ResourceFile;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResourceFileBo {

    private ResourceFile resourceFile;
    private String fileUrl;
    private InputStreamSource fileSource;

    public static ResourceFileBo fromMultipartFile(MultipartFile multipartFile) {
        ResourceFile resourceFile = new ResourceFile();
        resourceFile.setFileName(multipartFile.getOriginalFilename());
        resourceFile.setFileExtension(getFileExtension(multipartFile.getOriginalFilename()));
        resourceFile.setFileSize(multipartFile.getSize());
        resourceFile.setContentType(multipartFile.getContentType());

        ResourceFileBo resourceFileBo = new ResourceFileBo();
        resourceFileBo.setResourceFile(resourceFile);
        resourceFileBo.setFileSource(multipartFile);

        return resourceFileBo;
    }

    public static ResourceFileBo fromEntity(ResourceFile resourceFile, String fileUrl, InputStreamSource fileSource) {
        ResourceFileBo resourceFileBo = new ResourceFileBo();
        resourceFileBo.setResourceFile(resourceFile);
        resourceFileBo.setFileUrl(fileUrl);
        resourceFileBo.setFileSource(fileSource);

        return resourceFileBo;
    }

    public static ResourceFileBo fromBytes(byte[] imageData, String filename, String suffix, String contentType) {
        ByteArrayResource resource = new ByteArrayResource(imageData);

        ResourceFile resourceFile = new ResourceFile();
        resourceFile.setFileName(filename);
        resourceFile.setFileExtension(suffix);
        resourceFile.setFileSize(Long.valueOf(imageData.length));
        resourceFile.setContentType(contentType);

        ResourceFileBo resourceFileBo = new ResourceFileBo();
        resourceFileBo.setResourceFile(resourceFile);
        resourceFileBo.setFileSource(resource);
        return resourceFileBo;
    }

    private static String getFileExtension(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        int indexOfSp = fileName.lastIndexOf(".");
        if (indexOfSp == -1) {
            return null;
        }
        return fileName.substring(indexOfSp + 1);
    }

    public Long getId() {
        return resourceFile.getId();
    }

}
