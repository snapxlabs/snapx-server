package com.digcoin.snapx.domain.infra.component.exchangerates.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/10 11:55
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LatestReq {

    private String base;

    private String symbols;

}
