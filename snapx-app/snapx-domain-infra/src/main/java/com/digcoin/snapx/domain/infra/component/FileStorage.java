package com.digcoin.snapx.domain.infra.component;

import com.digcoin.snapx.domain.infra.bo.ResourceFileBo;
import com.digcoin.snapx.domain.infra.entity.ResourceFile;

public interface FileStorage {

    void save(ResourceFileBo resourceFileBo);

    ResourceFileBo load(ResourceFile resourceFile);

}
