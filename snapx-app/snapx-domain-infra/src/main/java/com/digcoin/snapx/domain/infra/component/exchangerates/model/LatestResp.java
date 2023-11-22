package com.digcoin.snapx.domain.infra.component.exchangerates.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/10 11:50
 * @description
 */
@NoArgsConstructor
@Data
public class LatestResp {

    @JsonProperty("base")
    private String base;
    @JsonProperty("date")
    private String date;
    @JsonProperty("rates")
    private RatesDTO rates;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("timestamp")
    private Integer timestamp;

    @NoArgsConstructor
    @Data
    public static class RatesDTO {
        @JsonProperty("AED")
        private Double aed;
        @JsonProperty("AFN")
        private Double afn;
        @JsonProperty("ALL")
        private Double all;
        @JsonProperty("AMD")
        private Double amd;
        @JsonProperty("ANG")
        private Double ang;
        @JsonProperty("AOA")
        private Double aoa;
        @JsonProperty("ARS")
        private Double ars;
        @JsonProperty("AUD")
        private Double aud;
        @JsonProperty("AWG")
        private Double awg;
        @JsonProperty("AZN")
        private Double azn;
        @JsonProperty("BAM")
        private Double bam;
        @JsonProperty("BBD")
        private Double bbd;
        @JsonProperty("BDT")
        private Double bdt;
        @JsonProperty("BGN")
        private Double bgn;
        @JsonProperty("BHD")
        private Double bhd;
        @JsonProperty("BIF")
        private Double bif;
        @JsonProperty("BMD")
        private Double bmd;
        @JsonProperty("BND")
        private Double bnd;
        @JsonProperty("BOB")
        private Double bob;
        @JsonProperty("BRL")
        private Double brl;
        @JsonProperty("BSD")
        private Double bsd;
        @JsonProperty("BTC")
        private Double btc;
        @JsonProperty("BTN")
        private Double btn;
        @JsonProperty("BWP")
        private Double bwp;
        @JsonProperty("BYN")
        private Double byn;
        @JsonProperty("BYR")
        private Double byr;
        @JsonProperty("BZD")
        private Double bzd;
        @JsonProperty("CAD")
        private Double cad;
        @JsonProperty("CDF")
        private Double cdf;
        @JsonProperty("CHF")
        private Double chf;
        @JsonProperty("CLF")
        private Double clf;
        @JsonProperty("CLP")
        private Double clp;
        @JsonProperty("CNY")
        private Double cny;
        @JsonProperty("COP")
        private Double cop;
        @JsonProperty("CRC")
        private Double crc;
        @JsonProperty("CUC")
        private Double cuc;
        @JsonProperty("CUP")
        private Double cup;
        @JsonProperty("CVE")
        private Double cve;
        @JsonProperty("CZK")
        private Double czk;
        @JsonProperty("DJF")
        private Double djf;
        @JsonProperty("DKK")
        private Double dkk;
        @JsonProperty("DOP")
        private Double dop;
        @JsonProperty("DZD")
        private Double dzd;
        @JsonProperty("EGP")
        private Double egp;
        @JsonProperty("ERN")
        private Double ern;
        @JsonProperty("ETB")
        private Double etb;
        @JsonProperty("EUR")
        private Double eur;
        @JsonProperty("FJD")
        private Double fjd;
        @JsonProperty("FKP")
        private Double fkp;
        @JsonProperty("GBP")
        private Double gbp;
        @JsonProperty("GEL")
        private Double gel;
        @JsonProperty("GGP")
        private Double ggp;
        @JsonProperty("GHS")
        private Double ghs;
        @JsonProperty("GIP")
        private Double gip;
        @JsonProperty("GMD")
        private Double gmd;
        @JsonProperty("GNF")
        private Double gnf;
        @JsonProperty("GTQ")
        private Double gtq;
        @JsonProperty("GYD")
        private Double gyd;
        @JsonProperty("HKD")
        private Double hkd;
        @JsonProperty("HNL")
        private Double hnl;
        @JsonProperty("HRK")
        private Double hrk;
        @JsonProperty("HTG")
        private Double htg;
        @JsonProperty("HUF")
        private Double huf;
        @JsonProperty("IDR")
        private Double idr;
        @JsonProperty("ILS")
        private Double ils;
        @JsonProperty("IMP")
        private Double imp;
        @JsonProperty("INR")
        private Double inr;
        @JsonProperty("IQD")
        private Double iqd;
        @JsonProperty("IRR")
        private Double irr;
        @JsonProperty("ISK")
        private Double isk;
        @JsonProperty("JEP")
        private Double jep;
        @JsonProperty("JMD")
        private Double jmd;
        @JsonProperty("JOD")
        private Double jod;
        @JsonProperty("JPY")
        private Double jpy;
        @JsonProperty("KES")
        private Double kes;
        @JsonProperty("KGS")
        private Double kgs;
        @JsonProperty("KHR")
        private Double khr;
        @JsonProperty("KMF")
        private Double kmf;
        @JsonProperty("KPW")
        private Double kpw;
        @JsonProperty("KRW")
        private Double krw;
        @JsonProperty("KWD")
        private Double kwd;
        @JsonProperty("KYD")
        private Double kyd;
        @JsonProperty("KZT")
        private Double kzt;
        @JsonProperty("LAK")
        private Double lak;
        @JsonProperty("LBP")
        private Double lbp;
        @JsonProperty("LKR")
        private Double lkr;
        @JsonProperty("LRD")
        private Double lrd;
        @JsonProperty("LSL")
        private Double lsl;
        @JsonProperty("LTL")
        private Double ltl;
        @JsonProperty("LVL")
        private Double lvl;
        @JsonProperty("LYD")
        private Double lyd;
        @JsonProperty("MAD")
        private Double mad;
        @JsonProperty("MDL")
        private Double mdl;
        @JsonProperty("MGA")
        private Double mga;
        @JsonProperty("MKD")
        private Double mkd;
        @JsonProperty("MMK")
        private Double mmk;
        @JsonProperty("MNT")
        private Double mnt;
        @JsonProperty("MOP")
        private Double mop;
        @JsonProperty("MRO")
        private Double mro;
        @JsonProperty("MUR")
        private Double mur;
        @JsonProperty("MVR")
        private Double mvr;
        @JsonProperty("MWK")
        private Double mwk;
        @JsonProperty("MXN")
        private Double mxn;
        @JsonProperty("MYR")
        private Double myr;
        @JsonProperty("MZN")
        private Double mzn;
        @JsonProperty("NAD")
        private Double nad;
        @JsonProperty("NGN")
        private Double ngn;
        @JsonProperty("NIO")
        private Double nio;
        @JsonProperty("NOK")
        private Double nok;
        @JsonProperty("NPR")
        private Double npr;
        @JsonProperty("NZD")
        private Double nzd;
        @JsonProperty("OMR")
        private Double omr;
        @JsonProperty("PAB")
        private Double pab;
        @JsonProperty("PEN")
        private Double pen;
        @JsonProperty("PGK")
        private Double pgk;
        @JsonProperty("PHP")
        private Double php;
        @JsonProperty("PKR")
        private Double pkr;
        @JsonProperty("PLN")
        private Double pln;
        @JsonProperty("PYG")
        private Double pyg;
        @JsonProperty("QAR")
        private Double qar;
        @JsonProperty("RON")
        private Double ron;
        @JsonProperty("RSD")
        private Double rsd;
        @JsonProperty("RUB")
        private Double rub;
        @JsonProperty("RWF")
        private Double rwf;
        @JsonProperty("SAR")
        private Double sar;
        @JsonProperty("SBD")
        private Double sbd;
        @JsonProperty("SCR")
        private Double scr;
        @JsonProperty("SDG")
        private Double sdg;
        @JsonProperty("SEK")
        private Double sek;
        @JsonProperty("SGD")
        private Double sgd;
        @JsonProperty("SHP")
        private Double shp;
        @JsonProperty("SLE")
        private Double sle;
        @JsonProperty("SLL")
        private Double sll;
        @JsonProperty("SOS")
        private Double sos;
        @JsonProperty("SRD")
        private Double srd;
        @JsonProperty("STD")
        private Double std;
        @JsonProperty("SVC")
        private Double svc;
        @JsonProperty("SYP")
        private Double syp;
        @JsonProperty("SZL")
        private Double szl;
        @JsonProperty("THB")
        private Double thb;
        @JsonProperty("TJS")
        private Double tjs;
        @JsonProperty("TMT")
        private Double tmt;
        @JsonProperty("TND")
        private Double tnd;
        @JsonProperty("TOP")
        private Double top;
        @JsonProperty("TRY")
        private Double tryX;
        @JsonProperty("TTD")
        private Double ttd;
        @JsonProperty("TWD")
        private Double twd;
        @JsonProperty("TZS")
        private Double tzs;
        @JsonProperty("UAH")
        private Double uah;
        @JsonProperty("UGX")
        private Double ugx;
        @JsonProperty("USD")
        private Double usd;
        @JsonProperty("UYU")
        private Double uyu;
        @JsonProperty("UZS")
        private Double uzs;
        @JsonProperty("VEF")
        private Double vef;
        @JsonProperty("VES")
        private Double ves;
        @JsonProperty("VND")
        private Double vnd;
        @JsonProperty("VUV")
        private Double vuv;
        @JsonProperty("WST")
        private Double wst;
        @JsonProperty("XAF")
        private Double xaf;
        @JsonProperty("XAG")
        private Double xag;
        @JsonProperty("XAU")
        private Double xau;
        @JsonProperty("XCD")
        private Double xcd;
        @JsonProperty("XDR")
        private Double xdr;
        @JsonProperty("XOF")
        private Double xof;
        @JsonProperty("XPF")
        private Double xpf;
        @JsonProperty("YER")
        private Double yer;
        @JsonProperty("ZAR")
        private Double zar;
        @JsonProperty("ZMK")
        private Double zmk;
        @JsonProperty("ZMW")
        private Double zmw;
        @JsonProperty("ZWL")
        private Double zwl;
    }

}
