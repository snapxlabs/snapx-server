package com.digcoin.snapx.domain.infra.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * guthub国家电话区号数据库
 * https://github.com/jjeejj/CountryCodeAndPhoneCode
 */
@Data
public class PhoneAreaCodeBO {

    @JsonProperty("english_name")
    private String englishName;

    @JsonProperty("chinese_name")
    private String chineseName;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("phone_code")
    private String phoneCode;

    @JsonProperty("emoji")
    private String emoji;
}
