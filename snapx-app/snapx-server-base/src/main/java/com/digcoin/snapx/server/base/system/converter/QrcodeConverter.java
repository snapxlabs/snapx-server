package com.digcoin.snapx.server.base.system.converter;

import com.digcoin.snapx.domain.system.entity.Qrcode;
import com.digcoin.snapx.server.base.system.dto.QrcodeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QrcodeConverter {

    QrcodeDTO intoDTO(Qrcode entity);


}
