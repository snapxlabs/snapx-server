package com.digcoin.snapx.core.mybatis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Schema(description = "分页对象")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "总记录数", type = "java.lang.Integer")
    private Integer totalCount;

    @Schema(description = "每页记录数", type = "java.lang.Integer")
    private Integer pageSize;

    @Schema(description = "总页数", type = "java.lang.Integer")
    private Integer totalPage;

    @Schema(description = "当前页数", type = "java.lang.Integer")
    private Integer currPage;

    @Schema(description = "页面数据")
    private List<T> data;


    public static <T, R> PageResult<R> fromPage(IPage<T> page, Function<T, R> mapping) {
        PageResult<R> pageResult = converter(page);
        pageResult.setData(page.getRecords()
                .stream()
                .map(mapping)
                .collect(Collectors.toList()));
        return pageResult;
    }

    public static <T, R> PageResult<R> fromPage(IPage<T> page, List<R> list) {
        PageResult<R> pageResult = converter(page);
        pageResult.setData(list);
        return pageResult;
    }

    public static <T, R> PageResult<R> fromPageResult(PageResult<T> page, List<R> list) {
        PageResult<R> pageResult = converter(page);
        return pageResult;
    }

    public static <S, T> PageResult<T> converter(List<S> src, List<T> target) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setData(target);
        if (src instanceof Page) {
            Page page = (Page) src;
            pageResult.setCurrPage(page.getPageNum());
            pageResult.setPageSize(page.getPageSize());
            pageResult.setTotalPage(page.getPages());
            pageResult.setTotalCount((int) page.getTotal());
        }
        return pageResult;
    }

    public static <T, R> PageResult<R> fromPageResult(PageResult<T> page, Function<T, R> mapping) {
        PageResult<R> pageResult = converter(page);
        pageResult.setData(page.getData()
                .stream()
                .map(mapping)
                .collect(Collectors.toList()));
        return pageResult;
    }

    public static <T, R> PageResult<R> fromPageResult2(PageResult<T> page, Function<Collection<T>, Function<T, R>> mappingProvider) {
        PageResult<R> pageResult = converter(page);
        List<T> list = page.getData();
        pageResult.setData(list
                .stream()
                .map(mappingProvider.apply(list))
                .collect(Collectors.toList()));
        return pageResult;
    }

    public static <R> PageResult<R> emptyResult() {
        PageResult<R> pageResult = new PageResult<R>();
        pageResult.setCurrPage(1);
        pageResult.setPageSize(10);
        pageResult.setTotalCount(0);
        pageResult.setTotalPage(0);
        pageResult.setData(Collections.emptyList());
        return pageResult;
    }

    private static <R> PageResult<R> converter(IPage page) {
        if (Objects.isNull(page)) {
            return emptyResult();
        }
        PageResult<R> pageResult = new PageResult<R>();
        pageResult.setCurrPage(Optional.of(page.getCurrent()).map(Long::intValue).orElse(1));
        pageResult.setPageSize(Optional.of(page.getSize()).map(Long::intValue).orElse(10));
        pageResult.setTotalCount(Optional.of(page.getTotal()).map(Long::intValue).orElse(0));
        pageResult.setTotalPage(Optional.of(page.getPages()).map(Long::intValue).orElse(0));
        return pageResult;
    }

    private static <R> PageResult<R> converter(PageResult page) {
        if (Objects.isNull(page)) {
            return emptyResult();
        }
        PageResult<R> pageResult = new PageResult<R>();
        pageResult.setCurrPage(Optional.ofNullable(page.getCurrPage()).orElse(1));
        pageResult.setPageSize(Optional.ofNullable(page.getPageSize()).orElse(10));
        pageResult.setTotalCount(Optional.ofNullable(page.getTotalCount()).orElse(0));
        pageResult.setTotalPage(Optional.ofNullable(page.getTotalPage()).orElse(0));
        return pageResult;
    }
}
