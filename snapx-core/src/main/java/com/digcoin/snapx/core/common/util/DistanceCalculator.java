package com.digcoin.snapx.core.common.util;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/3/14 17:43
 * @description
 */
public class DistanceCalculator {

    /**
     * 计算两个位置的距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离（单位:千米）
     */
    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        // 地球半径
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        return d;
    }

    /**
     * 计算两个位置的距离是否在范围内
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @param distanceRange 距离范围（单位:千米）
     * @return 结果
     */
    public static boolean within(double lat1, double lng1, double lat2, double lng2, double distanceRange) {
        return distance(lat1, lng1, lat2, lng2) <= distanceRange;
    }

    public static void main(String[] args) {
        System.out.println(distance(22.334703, 114.177224, 22.330177, 114.174478));
        // 0.5771054877192596(km)

        System.out.println(within(22.334703, 114.177224, 22.330177, 114.174478, 0.6));
        // true

        System.out.println(within(22.334703, 114.177224, 22.330177, 114.174478, 0.5));
        // false
    }

}
