package com.digcoin.snapx.core.mybatis.helper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digcoin.snapx.core.mybatis.Pageable;

import java.util.Optional;

public class PageHelper {

    public static <T> IPage<T> getPage(Pageable pageable) {
        Page<T> pager = new Page<>();
        pager.setCurrent((long) Optional.ofNullable(pageable.getPage()).orElse(1));
        pager.setSize((long) Optional.ofNullable(pageable.getPageSize()).orElse(10));
        return pager;
    }
}
