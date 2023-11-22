package com.digcoin.snapx.core.error;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class ErrorCodeManager {

    public static ErrorCodeManager getInstance() {
        return Holder.instance;
    }

    private static final BiFunction<String, String, String> keyGenerator = (group, name) -> group + "." + name;

    private static class Holder {
        private static final ErrorCodeManager instance = new ErrorCodeManager();
    }

    private final Map<String, ErrorCodeDefinition> errorCodeMap = new ConcurrentHashMap<>();

    public void update(ErrorCodeDefinition definition) {
        Objects.requireNonNull(definition);
        errorCodeMap.put(keyGenerator.apply(definition.getGroup(), definition.getName()), definition);
    }

    public void updateIfAbsent(ErrorCodeDefinition definition) {
        Objects.requireNonNull(definition);
        errorCodeMap.putIfAbsent(keyGenerator.apply(definition.getGroup(), definition.getName()), definition);
    }

    public ErrorCodeDefinition getErrorCode(String group, String name) {
        return errorCodeMap.get(keyGenerator.apply(group, name));
    }

    public static ExceptionBuilder exceptionBuilder() {
        return new ExceptionBuilder(ErrorCodeManager.getInstance());
    }

}
