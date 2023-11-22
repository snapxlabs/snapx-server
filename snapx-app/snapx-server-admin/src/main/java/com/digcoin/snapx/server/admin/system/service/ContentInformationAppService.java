package com.digcoin.snapx.server.admin.system.service;

import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.domain.system.bo.ContentInformationQuery;
import com.digcoin.snapx.domain.system.entity.ContentInformation;
import com.digcoin.snapx.domain.system.service.ContentInformationService;
import com.digcoin.snapx.server.base.system.converter.ContentInformationConverter;
import com.digcoin.snapx.server.base.system.dto.ContentInformationDTO;
import com.digcoin.snapx.server.base.system.dto.ContentInformationItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentInformationAppService {

    private final ContentInformationService contentInformationService;
    private final ContentInformationConverter contentInformationConverter;

    public PageResult<ContentInformationItemDTO> pageContentInformation(ContentInformationQuery query) {
        PageResult<ContentInformation> pageResult = contentInformationService.pageContentInformation(query);
        return PageResult.fromPageResult(pageResult, contentInformationConverter::intoItemDTO);
    }

    public ContentInformationDTO findContentInformation(Long id) {
        return contentInformationConverter.intoDTO(contentInformationService.findContentInformation(id));
    }

    public ContentInformationDTO createContentInformation(ContentInformationDTO contentInformation) {
        ContentInformation entity = contentInformationConverter.fromDTO(contentInformation);
        Long id = contentInformationService.createContentInformation(entity);
        return findContentInformation(id);
    }

    public ContentInformationDTO updateContentInformation(Long id, ContentInformationDTO contentInformation) {
        ContentInformation entity = contentInformationConverter.fromDTO(contentInformation);
        entity.setId(id);
        contentInformationService.updateContentInformation(entity);
        return findContentInformation(id);
    }

    public ContentInformationDTO updateContentInformationPublish(Long id, Boolean publish) {
        ContentInformation entity = new ContentInformation();
        entity.setId(id);
        entity.setPublish(publish);
        contentInformationService.updateContentInformation(entity);
        return findContentInformation(id);
    }

    public ContentInformationDTO deleteContentInformation(Long id) {
        ContentInformationDTO contentInformation = findContentInformation(id);
        contentInformationService.deleteContentInformation(id);
        return contentInformation;
    }



}
