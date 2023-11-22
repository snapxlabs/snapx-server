package com.digcoin.snapx.core.error.enums;

public class EnumErrorCodeExample {

    private enum HttpError implements EnumErrorCodeFactory {

        BAD_REQUEST("400", "Bad Request"),
        NOT_FOUND("404", "Not Found"),
        BAD_GATEWAY("502", " Bad Gateway"),
        SERVICE_UNAVAILABLE("503", "Service Unavailable");

        HttpError(String code, String message) {
            this.update(code, message);
        }

    }

}
