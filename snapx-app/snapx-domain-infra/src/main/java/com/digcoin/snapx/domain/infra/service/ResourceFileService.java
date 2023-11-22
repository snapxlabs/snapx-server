package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.infra.bo.ResourceFileBo;
import com.digcoin.snapx.domain.infra.component.FileStorage;
import com.digcoin.snapx.domain.infra.component.RekognitionStorage;
import com.digcoin.snapx.domain.infra.entity.ResourceFile;
import com.digcoin.snapx.domain.infra.mapper.ResourceFileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceFileService {

    private final ResourceFileMapper resourceFileMapper;
    private final FileStorage fileStorage;
    private final RekognitionStorage rekognitionStorage;

    @Transactional(rollbackFor = Exception.class)
    public Long createResourceFile(ResourceFileBo resourceFileBo) {
        ResourceFile resourceFile = resourceFileBo.getResourceFile();
        rekognitionStorage.imageModeration(resourceFileBo);
        resourceFileMapper.insert(resourceFile);
        fileStorage.save(resourceFileBo);
        return resourceFile.getId();
    }

    public ResourceFileBo findResourceFile(Long id) {
        return toResourceFileBo(resourceFileMapper.selectById(id));
    }

    public Map<Long, ResourceFileBo> mapResourceFile(Collection<Long> ids) {
        return listResourceFile(ids).stream().collect(Collectors.toMap(ResourceFileBo::getId, Function.identity()));
    }

    public List<ResourceFileBo> listResourceFile(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ResourceFile> resourceFileList = resourceFileMapper.selectList(Wrappers.lambdaQuery(ResourceFile.class)
                .in(ResourceFile::getId, ids));

        return resourceFileList.stream().map(this::toResourceFileBo).collect(Collectors.toList());
    }

    private ResourceFileBo toResourceFileBo(ResourceFile resourceFile) {
        return fileStorage.load(resourceFile);
    }

}
