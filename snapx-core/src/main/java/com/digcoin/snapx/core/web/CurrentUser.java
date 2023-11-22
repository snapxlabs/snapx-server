package com.digcoin.snapx.core.web;

import com.digcoin.snapx.core.common.enums.Gender;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class CurrentUser implements Serializable {

    public static final String IDENTITY = "com.digcoin.snapx.core.web.CurrentUser";

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String nickname;

    private String name;

    private String phone;

    private String mail;

    private Gender gender;

    private String avatar;

    private Map<String, String> extra;

}
