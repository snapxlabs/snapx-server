package com.digcoin.snapx.domain.camera.error;

import com.digcoin.snapx.core.error.enums.EnumErrorCodeFactory;

/**
 * @author rui.wang
 * @version 1.0.0
 * @date 2023/2/23 15:20
 * @description
 */
public enum FilmMemberError implements EnumErrorCodeFactory {

    FILM_BALANCE_INSUFFICIENT("101000", "Film balance is insufficient."),
    FILM_BALANCE_UPDATE_FAILED("101000", "Film balance updated failed.");

    FilmMemberError(String code, String message) {
        this.update(code, message);
    }
}
