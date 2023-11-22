package com.digcoin.snapx.core.redis;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/24 10:00
 * @description
 */
public interface RedisConstant {

    String SYSTEM_SETTING = "system-setting";

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

}
