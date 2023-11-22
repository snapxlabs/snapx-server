package com.digcoin.snapx.domain.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.system.bo.ContentInformationQuery;
import com.digcoin.snapx.domain.system.entity.ContentInformation;
import com.digcoin.snapx.domain.system.mapper.ContentInformationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentInformationService {

    private final ContentInformationMapper contentInformationMapper;

    public List<ContentInformation> listPublishContentInformation(String type, Integer limit) {
        return contentInformationMapper.selectList(Wrappers.lambdaQuery(ContentInformation.class)
                        .select(ContentInformation.class, field -> !"content".equals(field.getColumn()))
                .eq(ContentInformation::getPublish, true)
                .eq(ContentInformation::getInformationType, type)
                .orderByAsc(ContentInformation::getSort)
                .last(String.format("limit %d",limit)));
    }

    public PageResult<ContentInformation> pageContentInformation(ContentInformationQuery query) {
        IPage<ContentInformation> page = contentInformationMapper.selectPage(PageHelper.getPage(query),
                Wrappers.lambdaQuery(ContentInformation.class)
                        .select(ContentInformation.class, field -> !"content".equals(field.getColumn()))
                        .eq(StringUtils.isNotBlank(query.getInformationType()), ContentInformation::getInformationType, query.getInformationType())
                        .eq(Objects.nonNull(query.getPublish()), ContentInformation::getPublish, query.getPublish())
                        .like(StringUtils.isNotBlank(query.getTitle()), ContentInformation::getTitle, query.getTitle())
                        .like(StringUtils.isNotBlank(query.getSummary()), ContentInformation::getSummary, query.getSummary())
                        .orderByAsc(ContentInformation::getSort)
                        .orderByDesc(ContentInformation::getCreateTime)
        );
        return PageResult.fromPage(page, Function.identity());
    }

    public ContentInformation findContentInformation(Long id) {
        return contentInformationMapper.selectById(id);
    }

    public Long createContentInformation(ContentInformation entity) {
        contentInformationMapper.insert(entity);
        return entity.getId();
    }

    public void updateContentInformation(ContentInformation entity) {
        contentInformationMapper.updateById(entity);
    }

    public void deleteContentInformation(Long id) {
        contentInformationMapper.deleteById(id);
    }
}
