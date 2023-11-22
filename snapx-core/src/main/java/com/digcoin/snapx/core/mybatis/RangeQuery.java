package com.digcoin.snapx.core.mybatis;

public interface RangeQuery<T> {

    /**
     * 获取查询范围开始点
     * @return
     */
    T getStarting();

    /**
     * 获取查询范围结束点
     * @return
     */
    T getEnding();

}
