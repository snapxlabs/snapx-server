package com.digcoin.snapx.domain.infra.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * github 国家信息数据库
 * https://raw.githubusercontent.com/dr5hn/countries-states-cities-database/master/countries.json
 */
@Data
public class CountryBO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("iso3")
    private String iso3;

    @JsonProperty("iso2")
    private String iso2;

    @JsonProperty("numeric_code")
    private String numericCode;

    @JsonProperty("phone_code")
    private String phoneCode;

    @JsonProperty("capital")
    private String capital;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("currency_name")
    private String currencyName;

    @JsonProperty("currency_symbol")
    private String currencySymbol;

    @JsonProperty("tld")
    private String tld;

    @JsonProperty("native")
    private String nativeName;

    @JsonProperty("region")
    private String region;

    @JsonProperty("subregion")
    private String subregion;

    @JsonProperty("timezones")
    private Timezone[] timezones;

    @JsonProperty("translations")
    private Translations translations;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("emoji")
    private String emoji;

    @JsonProperty("emojiU")
    private String emojiU;

    @Data
    public static class Timezone {

        @JsonProperty("zoneName")
        private String zoneName;

        @JsonProperty("gmtOffset")
        private int gmtOffset;

        @JsonProperty("gmtOffsetName")
        private String gmtOffsetName;

        @JsonProperty("abbreviation")
        private String abbreviation;

        @JsonProperty("tzName")
        private String tzName;

    }

    @Data
    public static class Translations {

        @JsonProperty("kr")
        private String kr;

        @JsonProperty("pt-BR")
        private String ptBR;

        @JsonProperty("pt")
        private String pt;

        @JsonProperty("nl")
        private String nl;

        @JsonProperty("hr")
        private String hr;

        @JsonProperty("fa")
        private String fa;

        @JsonProperty("de")
        private String de;

        @JsonProperty("es")
        private String es;

        @JsonProperty("fr")
        private String fr;

        @JsonProperty("ja")
        private String ja;

        @JsonProperty("it")
        private String it;

        @JsonProperty("cn")
        private String cn;

        @JsonProperty("tr")
        private String tr;

    }
}
