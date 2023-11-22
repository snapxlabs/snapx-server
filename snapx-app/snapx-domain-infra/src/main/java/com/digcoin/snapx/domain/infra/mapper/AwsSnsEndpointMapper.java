package com.digcoin.snapx.domain.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digcoin.snapx.domain.infra.entity.AwsSnsEndpoint;
import org.apache.ibatis.annotations.Param;

public interface AwsSnsEndpointMapper extends BaseMapper<AwsSnsEndpoint> {

    void saveOrUpdateAwsSnsEndpoint(@Param("p") AwsSnsEndpoint endpoint);

}
