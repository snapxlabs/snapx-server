package com.digcoin.snapx.domain.infra.component;

import com.digcoin.snapx.domain.infra.bo.ResourceFileBo;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/18 15:58
 * @description
 */
public interface RekognitionStorage {

    void imageModeration(ResourceFileBo resourceFileBo);

}
