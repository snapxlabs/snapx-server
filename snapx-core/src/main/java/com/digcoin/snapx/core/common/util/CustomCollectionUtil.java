package com.digcoin.snapx.core.common.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/4 13:24
 * @description
 */
public class CustomCollectionUtil {

    public static <T, R> List<R> listColumn(List<T> list, Function<T, R> column) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(column).collect(Collectors.toList());
    }

    public static <K, T> Map<K, T> mapping(List<T> list, Function<T, K> column) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(column, Function.identity()));
    }

}
