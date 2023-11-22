package com.digcoin.snapx.server.app.infra.converter;

import com.digcoin.snapx.domain.infra.entity.PhoneAreaCode;
import com.digcoin.snapx.server.app.infra.dto.PhoneAreaCodeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneAreaCodeConverter {

    PhoneAreaCodeDTO fromEntity(PhoneAreaCode entity);

}
