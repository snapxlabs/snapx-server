package com.digcoin.snapx.domain.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digcoin.snapx.domain.system.entity.Version;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/13 16:14
 * @description
 */
@Mapper
public interface VersionMapper extends BaseMapper<Version> {
}
