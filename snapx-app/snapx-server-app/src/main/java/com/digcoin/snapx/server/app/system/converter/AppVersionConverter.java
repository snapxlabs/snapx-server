package com.digcoin.snapx.server.app.system.converter;

import com.digcoin.snapx.domain.system.entity.Version;
import com.digcoin.snapx.server.app.system.vo.AppVersionVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/5/14 13:06
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppVersionConverter {

    AppVersionVO intoVO(Version version);

}
