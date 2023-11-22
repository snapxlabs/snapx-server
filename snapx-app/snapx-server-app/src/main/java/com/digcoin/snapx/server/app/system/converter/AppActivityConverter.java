package com.digcoin.snapx.server.app.system.converter;

import com.digcoin.snapx.domain.system.entity.Activity;
import com.digcoin.snapx.server.app.system.vo.AppActivityVO;
import org.mapstruct.Mapper;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/4/5 18:16
 * @description
 */
@Mapper(componentModel = "spring")
public interface AppActivityConverter {

    AppActivityVO intoVO(Activity activity);

}
