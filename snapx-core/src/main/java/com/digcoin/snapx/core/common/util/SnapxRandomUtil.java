package com.digcoin.snapx.core.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/1/10 16:52
 * @description
 */
public class SnapxRandomUtil {

    private static final Random RANDOM = new Random();

    public static String getRandomString(int length) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(String.valueOf(RANDOM.nextInt(9)));
        }
        return String.join("", list);
    }

    public static BigDecimal getRandom(Double min, Double max) {
        if (max.equals(min)) {
            return BigDecimal.valueOf(min).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(Math.random() * (max - min) + min).setScale(2, RoundingMode.HALF_UP);
    }
}
