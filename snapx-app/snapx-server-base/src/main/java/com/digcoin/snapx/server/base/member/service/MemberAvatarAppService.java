package com.digcoin.snapx.server.base.member.service;

import com.digcoin.snapx.domain.system.constant.MaterialGalleryGroup;
import com.digcoin.snapx.domain.system.constant.MaterialGalleryType;
import com.digcoin.snapx.domain.system.entity.MaterialGallery;
import com.digcoin.snapx.domain.system.service.ImageMaterialGalleryService;
import com.digcoin.snapx.server.base.member.dto.MemberAvatarDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberAvatarAppService {

    private final ImageMaterialGalleryService imageMaterialGalleryService;

    public List<MemberAvatarDTO> listMemberAvatar() {
        List<MaterialGallery> avatarList = imageMaterialGalleryService.listByGroup(MaterialGalleryGroup.AVATAR);
        List<MaterialGallery> avatarCoverList = imageMaterialGalleryService.listByGroup(MaterialGalleryGroup.AVATAR_COVER);

        List<MemberAvatarDTO> resultList = new ArrayList<>(avatarList.size());
        for (int i = 0; i < avatarList.size(); i++) {
            MemberAvatarDTO materialGallery = new MemberAvatarDTO();
            materialGallery.setAvatarMaterialGalleryId(avatarList.get(i).getId());
            materialGallery.setAvatarUrl(avatarList.get(i).getResourceFileUrl());
            materialGallery.setSort(avatarList.get(i).getSort());
            if (i < avatarCoverList.size()) {
                materialGallery.setAvatarCoverMaterialGalleryId(avatarCoverList.get(i).getId());
                materialGallery.setAvatarCoverUrl(avatarCoverList.get(i).getResourceFileUrl());
            }
            resultList.add(materialGallery);
        }
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createMemberAvatar(MemberAvatarDTO memberAvatar) {

        Integer sort = imageMaterialGalleryService.getMaxSort(MaterialGalleryGroup.AVATAR) + 1;

        MaterialGallery avatar = new MaterialGallery();
        avatar.setMaterialType(MaterialGalleryType.IMAGE);
        avatar.setMaterialGroup(MaterialGalleryGroup.AVATAR);
        avatar.setResourceFileId(memberAvatar.getAvatarResourceFileId());
        avatar.setResourceFileUrl(memberAvatar.getAvatarUrl());
        avatar.setSort(sort);
        imageMaterialGalleryService.createMaterialGallery(avatar);

        MaterialGallery avatarCover = new MaterialGallery();
        avatarCover.setMaterialType(MaterialGalleryType.IMAGE);
        avatarCover.setMaterialGroup(MaterialGalleryGroup.AVATAR_COVER);
        avatarCover.setResourceFileId(memberAvatar.getAvatarCoverResourceFileId());
        avatarCover.setResourceFileUrl(memberAvatar.getAvatarCoverUrl());
        avatarCover.setSort(sort);
        imageMaterialGalleryService.createMaterialGallery(avatarCover);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMemberAvatar(MemberAvatarDTO memberAvatar) {
        MaterialGallery avatar = new MaterialGallery();
        avatar.setId(memberAvatar.getAvatarMaterialGalleryId());
        avatar.setMaterialType(MaterialGalleryType.IMAGE);
        avatar.setMaterialGroup(MaterialGalleryGroup.AVATAR);
        avatar.setResourceFileId(memberAvatar.getAvatarResourceFileId());
        avatar.setResourceFileUrl(memberAvatar.getAvatarUrl());
        avatar.setSort(memberAvatar.getSort());
        imageMaterialGalleryService.updateMaterialGallery(avatar);

        MaterialGallery avatarCover = new MaterialGallery();
        avatarCover.setId(memberAvatar.getAvatarCoverMaterialGalleryId());
        avatarCover.setMaterialType(MaterialGalleryType.IMAGE);
        avatarCover.setMaterialGroup(MaterialGalleryGroup.AVATAR_COVER);
        avatarCover.setResourceFileId(memberAvatar.getAvatarCoverResourceFileId());
        avatarCover.setResourceFileUrl(memberAvatar.getAvatarCoverUrl());
        avatarCover.setSort(memberAvatar.getSort());
        imageMaterialGalleryService.updateMaterialGallery(avatarCover);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMemberAvatar(Collection<Long> ids) {
        imageMaterialGalleryService.deleteMaterialGallery(ids);
    }

}
