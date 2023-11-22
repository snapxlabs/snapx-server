package com.digcoin.snapx.domain.system.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.domain.system.constant.MaterialGalleryType;
import com.digcoin.snapx.domain.system.entity.MaterialGallery;
import com.digcoin.snapx.domain.system.mapper.MaterialGalleryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageMaterialGalleryService {

    private static final String TYPE = MaterialGalleryType.IMAGE;
    private final MaterialGalleryMapper materialGalleryMapper;

    public List<MaterialGallery> listByGroup(String group) {
        return materialGalleryMapper.selectList(Wrappers.lambdaQuery(MaterialGallery.class)
                .eq(MaterialGallery::getMaterialType, TYPE)
                .eq(MaterialGallery::getMaterialGroup, group)
                .orderByAsc(MaterialGallery::getSort));
    }

    public Integer getMaxSort(String group) {
        return Optional.ofNullable(materialGalleryMapper.selectOne(Wrappers.lambdaQuery(MaterialGallery.class)
                .eq(MaterialGallery::getMaterialType, TYPE)
                .eq(MaterialGallery::getMaterialGroup, group)
                .orderByDesc(MaterialGallery::getSort)
                        .last("limit 1")))
                .map(MaterialGallery::getSort)
                .orElse(0);
    }

    public Long createMaterialGallery(MaterialGallery entity) {
        materialGalleryMapper.insert(entity);
        return entity.getId();
    }

    public void updateMaterialGallery(MaterialGallery entity) {
        materialGalleryMapper.updateById(entity);
    }

    public void deleteMaterialGallery(Collection<Long> ids) {
        materialGalleryMapper.deleteBatchIds(ids);
    }

}

