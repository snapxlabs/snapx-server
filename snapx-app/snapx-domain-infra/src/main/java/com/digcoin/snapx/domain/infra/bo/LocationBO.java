package com.digcoin.snapx.domain.infra.bo;

import com.digcoin.snapx.domain.infra.entity.GeoCountry;
import com.digcoin.snapx.domain.infra.entity.GeoLocality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/2/24 20:40
 * @description
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationBO {

    private GeoCountry country;

    private GeoLocality locality;

}
