package com.digcoin.snapx.core.error;

import java.io.Serializable;
import java.util.Objects;

public final class ErrorCodeDefinition implements Transformable<ErrorCodeDefinition>, Serializable {

    private static final long serialVersionUID = 100088180398680873L;

    private final Class<? extends ExceptionTranslator<ErrorCodeDefinition, ? extends RuntimeException>> translatorClass;
    private final String group;
    private final String name;
    private final String code;
    private final String message;

    public ErrorCodeDefinition(Class<? extends ExceptionTranslator<ErrorCodeDefinition, ? extends RuntimeException>> translatorClass,
                               String group, String name, String code, String message) {
        this.translatorClass = translatorClass;
        this.group = group;
        this.name = name;
        this.code = code;
        this.message = message;
    }

    @Override
    public ErrorCodeDefinition getSelf() {
        return this;
    }

    public String orMessage(String message) {
        if (Objects.nonNull(message) && message.trim().length() > 0) {
            return message;
        }
        return this.message;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Class<? extends ExceptionTranslator<ErrorCodeDefinition, ? extends RuntimeException>> getTranslatorClass() {
        return translatorClass;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ErrorCodeDefinition) obj;
        return Objects.equals(this.group, that.group) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, name, code, message);
    }

    @Override
    public String toString() {
        return "ErrorCodeDefinition[" +
                "group=" + group + ", " +
                "name=" + name + ", " +
                "code=" + code + ", " +
                "message=" + message + ']';
    }

}
