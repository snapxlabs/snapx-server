package com.digcoin.snapx.server.app.system.service;

import com.digcoin.snapx.domain.system.service.ContentInformationService;
import com.digcoin.snapx.server.base.system.converter.ContentInformationConverter;
import com.digcoin.snapx.server.base.system.dto.ContentInformationDTO;
import com.digcoin.snapx.server.base.system.dto.ContentInformationItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentInformationAppService {

    private final ContentInformationService contentInformationService;
    private final ContentInformationConverter contentInformationConverter;

    public List<ContentInformationItemDTO> listPublishContentInformation(String type, Integer limit) {
        return contentInformationService.listPublishContentInformation(type, limit).stream()
                .map(contentInformationConverter::intoItemDTO)
                .collect(Collectors.toList());
    }

    public ContentInformationDTO getContentInformation(Long id) {
        return contentInformationConverter.intoDTO(contentInformationService.findContentInformation(id));
    }

}
