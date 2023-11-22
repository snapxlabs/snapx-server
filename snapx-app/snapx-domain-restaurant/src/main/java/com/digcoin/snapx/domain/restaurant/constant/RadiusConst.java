package com.digcoin.snapx.domain.restaurant.constant;

import java.math.BigDecimal;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/3 09:44
 * @description
 */
public class RadiusConst {

    public static final BigDecimal RADIUS_5KM = new BigDecimal(5);

    public static final BigDecimal RADIUS_500M = new BigDecimal(5).divide(new BigDecimal(10));

    public static final BigDecimal RADIUS_20KM = new BigDecimal(20);

}
