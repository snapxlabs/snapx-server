package com.digcoin.snapx.server.base.infra.converter;

import com.digcoin.snapx.domain.infra.entity.Currency;
import com.digcoin.snapx.server.base.infra.dto.CurrencyDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/3 22:35
 * @description
 */
@Mapper(componentModel = "spring")
public interface BaseCurrencyConverter {

    CurrencyDTO toDTO(Currency currency);

    List<CurrencyDTO> toDTO(List<Currency> currency);

}
