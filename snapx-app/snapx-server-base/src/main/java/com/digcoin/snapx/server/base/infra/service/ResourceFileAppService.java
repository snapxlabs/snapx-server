package com.digcoin.snapx.server.base.infra.service;

import com.digcoin.snapx.domain.infra.bo.ResourceFileBo;
import com.digcoin.snapx.domain.infra.service.ResourceFileService;
import com.digcoin.snapx.server.base.infra.converter.ResourceFileConverter;
import com.digcoin.snapx.server.base.infra.dto.ResourceFileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceFileAppService {

    private final ResourceFileService resourceFileService;
    private final ResourceFileConverter resourceFileConverter;

    public ResourceFileDTO createResourceFile(MultipartFile multipartFile) {
        Long resourceFileId = resourceFileService.createResourceFile(ResourceFileBo.fromMultipartFile(multipartFile));
        return findResourceFile(resourceFileId);
    }

    public ResourceFileDTO findResourceFile(Long id) {
        ResourceFileBo resourceFileBo = resourceFileService.findResourceFile(id);
        ResourceFileDTO resourceFileDTO = resourceFileConverter.intoDTO(resourceFileBo.getResourceFile());
        resourceFileDTO.setFileUrl(resourceFileBo.getFileUrl());
        return resourceFileDTO;
    }

}
