package com.digcoin.snapx.core.common.util;

public class BitFlagUtil {

    public static long turnOn(long status, long flagMask) {
        return status | flagMask;
    }

    public static long turnOff(long status, long flagMask) {
        return status ^ flagMask;
    }

    public static boolean isOn(long status, long flagMask) {
        return (status & flagMask) == flagMask;
    }

}
