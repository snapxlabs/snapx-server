package com.digcoin.snapx.domain.system.bo;

import lombok.Data;

import java.util.Objects;
import java.util.Optional;

/**
 * @author gengyang.chen
 * @version 1.0.0
 * @Description: APP版本号数值对象，用于比较版本号
 * @ClassName: VersionNumber
 * @date 2020/10/5 4:31 下午
 */
@Data
public class VersionNumber {

    private final Integer mainVerNumber;

    private final Integer middleVerNumber;

    private final Integer lastVerNumber;

    public static VersionNumber of(String version) {
        if (Objects.isNull(version) || version.trim().length() == 0) {
            return new VersionNumber(0, 0, 0);
        }
        String[] numbers = version.split("\\.");
        Integer mainVerNumber = numbers.length > 0 ? Integer.valueOf(numbers[0]) : Integer.valueOf(0);
        Integer middleVerNumber = numbers.length > 1 ? Integer.valueOf(numbers[1]) : Integer.valueOf(0);
        Integer lastVerNumber = numbers.length > 2 ? Integer.valueOf(numbers[2]) : Integer.valueOf(0);
        return new VersionNumber(mainVerNumber, middleVerNumber, lastVerNumber);
    }

    public VersionNumber(Integer mainVerNumber, Integer middleVerNumber, Integer lastVerNumber) {
        this.mainVerNumber = Optional.ofNullable(mainVerNumber).orElse(0);
        this.middleVerNumber = Optional.ofNullable(middleVerNumber).orElse(0);
        this.lastVerNumber = Optional.ofNullable(lastVerNumber).orElse(0);
    }

    public boolean isGreaterThan(VersionNumber version) {
        if (equals(version)) {
            return false;
        }

        if (mainVerNumber > version.getMainVerNumber()) {
            return true;
        }

        if (mainVerNumber.equals(version.getMainVerNumber())
                && middleVerNumber > version.getMiddleVerNumber()) {
            return true;
        }

        return mainVerNumber.equals(version.getMainVerNumber())
                && middleVerNumber.equals(version.getMiddleVerNumber())
                && lastVerNumber > version.getLastVerNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VersionNumber that = (VersionNumber) o;
        return Objects.equals(mainVerNumber, that.mainVerNumber) &&
                Objects.equals(middleVerNumber, that.middleVerNumber) &&
                Objects.equals(lastVerNumber, that.lastVerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainVerNumber, middleVerNumber, lastVerNumber);
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d", mainVerNumber, middleVerNumber, lastVerNumber);
    }
}
