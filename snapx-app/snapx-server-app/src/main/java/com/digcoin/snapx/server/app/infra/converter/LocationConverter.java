package com.digcoin.snapx.server.app.infra.converter;

import com.digcoin.snapx.domain.infra.bo.LocationBO;
import com.digcoin.snapx.server.app.infra.vo.LocationVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/6 22:41
 * @description
 */
@Mapper(componentModel = "spring")

public interface LocationConverter {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.longName", target = "countryName")
    @Mapping(source = "locality.id", target = "localityId")
    @Mapping(source = "locality.longName", target = "localityName")
    LocationVO toVO(LocationBO location);

    List<LocationVO> toVO(List<LocationBO> list);

}
