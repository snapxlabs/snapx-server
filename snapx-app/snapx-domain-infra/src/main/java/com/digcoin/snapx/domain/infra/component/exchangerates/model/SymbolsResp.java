package com.digcoin.snapx.domain.infra.component.exchangerates.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/5/10 11:59
 * @description
 */
@NoArgsConstructor
@Data
public class SymbolsResp {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("symbols")
    private SymbolsDTO symbols;

    @NoArgsConstructor
    @Data
    public static class SymbolsDTO {
        @JsonProperty("AED")
        private String aed;
        @JsonProperty("AFN")
        private String afn;
        @JsonProperty("ALL")
        private String all;
        @JsonProperty("AMD")
        private String amd;
        @JsonProperty("ANG")
        private String ang;
        @JsonProperty("AOA")
        private String aoa;
        @JsonProperty("ARS")
        private String ars;
        @JsonProperty("AUD")
        private String aud;
        @JsonProperty("AWG")
        private String awg;
        @JsonProperty("AZN")
        private String azn;
        @JsonProperty("BAM")
        private String bam;
        @JsonProperty("BBD")
        private String bbd;
        @JsonProperty("BDT")
        private String bdt;
        @JsonProperty("BGN")
        private String bgn;
        @JsonProperty("BHD")
        private String bhd;
        @JsonProperty("BIF")
        private String bif;
        @JsonProperty("BMD")
        private String bmd;
        @JsonProperty("BND")
        private String bnd;
        @JsonProperty("BOB")
        private String bob;
        @JsonProperty("BRL")
        private String brl;
        @JsonProperty("BSD")
        private String bsd;
        @JsonProperty("BTC")
        private String btc;
        @JsonProperty("BTN")
        private String btn;
        @JsonProperty("BWP")
        private String bwp;
        @JsonProperty("BYN")
        private String byn;
        @JsonProperty("BYR")
        private String byr;
        @JsonProperty("BZD")
        private String bzd;
        @JsonProperty("CAD")
        private String cad;
        @JsonProperty("CDF")
        private String cdf;
        @JsonProperty("CHF")
        private String chf;
        @JsonProperty("CLF")
        private String clf;
        @JsonProperty("CLP")
        private String clp;
        @JsonProperty("CNY")
        private String cny;
        @JsonProperty("COP")
        private String cop;
        @JsonProperty("CRC")
        private String crc;
        @JsonProperty("CUC")
        private String cuc;
        @JsonProperty("CUP")
        private String cup;
        @JsonProperty("CVE")
        private String cve;
        @JsonProperty("CZK")
        private String czk;
        @JsonProperty("DJF")
        private String djf;
        @JsonProperty("DKK")
        private String dkk;
        @JsonProperty("DOP")
        private String dop;
        @JsonProperty("DZD")
        private String dzd;
        @JsonProperty("EGP")
        private String egp;
        @JsonProperty("ERN")
        private String ern;
        @JsonProperty("ETB")
        private String etb;
        @JsonProperty("EUR")
        private String eur;
        @JsonProperty("FJD")
        private String fjd;
        @JsonProperty("FKP")
        private String fkp;
        @JsonProperty("GBP")
        private String gbp;
        @JsonProperty("GEL")
        private String gel;
        @JsonProperty("GGP")
        private String ggp;
        @JsonProperty("GHS")
        private String ghs;
        @JsonProperty("GIP")
        private String gip;
        @JsonProperty("GMD")
        private String gmd;
        @JsonProperty("GNF")
        private String gnf;
        @JsonProperty("GTQ")
        private String gtq;
        @JsonProperty("GYD")
        private String gyd;
        @JsonProperty("HKD")
        private String hkd;
        @JsonProperty("HNL")
        private String hnl;
        @JsonProperty("HRK")
        private String hrk;
        @JsonProperty("HTG")
        private String htg;
        @JsonProperty("HUF")
        private String huf;
        @JsonProperty("IDR")
        private String idr;
        @JsonProperty("ILS")
        private String ils;
        @JsonProperty("IMP")
        private String imp;
        @JsonProperty("INR")
        private String inr;
        @JsonProperty("IQD")
        private String iqd;
        @JsonProperty("IRR")
        private String irr;
        @JsonProperty("ISK")
        private String isk;
        @JsonProperty("JEP")
        private String jep;
        @JsonProperty("JMD")
        private String jmd;
        @JsonProperty("JOD")
        private String jod;
        @JsonProperty("JPY")
        private String jpy;
        @JsonProperty("KES")
        private String kes;
        @JsonProperty("KGS")
        private String kgs;
        @JsonProperty("KHR")
        private String khr;
        @JsonProperty("KMF")
        private String kmf;
        @JsonProperty("KPW")
        private String kpw;
        @JsonProperty("KRW")
        private String krw;
        @JsonProperty("KWD")
        private String kwd;
        @JsonProperty("KYD")
        private String kyd;
        @JsonProperty("KZT")
        private String kzt;
        @JsonProperty("LAK")
        private String lak;
        @JsonProperty("LBP")
        private String lbp;
        @JsonProperty("LKR")
        private String lkr;
        @JsonProperty("LRD")
        private String lrd;
        @JsonProperty("LSL")
        private String lsl;
        @JsonProperty("LTL")
        private String ltl;
        @JsonProperty("LVL")
        private String lvl;
        @JsonProperty("LYD")
        private String lyd;
        @JsonProperty("MAD")
        private String mad;
        @JsonProperty("MDL")
        private String mdl;
        @JsonProperty("MGA")
        private String mga;
        @JsonProperty("MKD")
        private String mkd;
        @JsonProperty("MMK")
        private String mmk;
        @JsonProperty("MNT")
        private String mnt;
        @JsonProperty("MOP")
        private String mop;
        @JsonProperty("MRO")
        private String mro;
        @JsonProperty("MUR")
        private String mur;
        @JsonProperty("MVR")
        private String mvr;
        @JsonProperty("MWK")
        private String mwk;
        @JsonProperty("MXN")
        private String mxn;
        @JsonProperty("MYR")
        private String myr;
        @JsonProperty("MZN")
        private String mzn;
        @JsonProperty("NAD")
        private String nad;
        @JsonProperty("NGN")
        private String ngn;
        @JsonProperty("NIO")
        private String nio;
        @JsonProperty("NOK")
        private String nok;
        @JsonProperty("NPR")
        private String npr;
        @JsonProperty("NZD")
        private String nzd;
        @JsonProperty("OMR")
        private String omr;
        @JsonProperty("PAB")
        private String pab;
        @JsonProperty("PEN")
        private String pen;
        @JsonProperty("PGK")
        private String pgk;
        @JsonProperty("PHP")
        private String php;
        @JsonProperty("PKR")
        private String pkr;
        @JsonProperty("PLN")
        private String pln;
        @JsonProperty("PYG")
        private String pyg;
        @JsonProperty("QAR")
        private String qar;
        @JsonProperty("RON")
        private String ron;
        @JsonProperty("RSD")
        private String rsd;
        @JsonProperty("RUB")
        private String rub;
        @JsonProperty("RWF")
        private String rwf;
        @JsonProperty("SAR")
        private String sar;
        @JsonProperty("SBD")
        private String sbd;
        @JsonProperty("SCR")
        private String scr;
        @JsonProperty("SDG")
        private String sdg;
        @JsonProperty("SEK")
        private String sek;
        @JsonProperty("SGD")
        private String sgd;
        @JsonProperty("SHP")
        private String shp;
        @JsonProperty("SLE")
        private String sle;
        @JsonProperty("SLL")
        private String sll;
        @JsonProperty("SOS")
        private String sos;
        @JsonProperty("SRD")
        private String srd;
        @JsonProperty("STD")
        private String std;
        @JsonProperty("SVC")
        private String svc;
        @JsonProperty("SYP")
        private String syp;
        @JsonProperty("SZL")
        private String szl;
        @JsonProperty("THB")
        private String thb;
        @JsonProperty("TJS")
        private String tjs;
        @JsonProperty("TMT")
        private String tmt;
        @JsonProperty("TND")
        private String tnd;
        @JsonProperty("TOP")
        private String top;
        @JsonProperty("TRY")
        private String tryX;
        @JsonProperty("TTD")
        private String ttd;
        @JsonProperty("TWD")
        private String twd;
        @JsonProperty("TZS")
        private String tzs;
        @JsonProperty("UAH")
        private String uah;
        @JsonProperty("UGX")
        private String ugx;
        @JsonProperty("USD")
        private String usd;
        @JsonProperty("UYU")
        private String uyu;
        @JsonProperty("UZS")
        private String uzs;
        @JsonProperty("VEF")
        private String vef;
        @JsonProperty("VES")
        private String ves;
        @JsonProperty("VND")
        private String vnd;
        @JsonProperty("VUV")
        private String vuv;
        @JsonProperty("WST")
        private String wst;
        @JsonProperty("XAF")
        private String xaf;
        @JsonProperty("XAG")
        private String xag;
        @JsonProperty("XAU")
        private String xau;
        @JsonProperty("XCD")
        private String xcd;
        @JsonProperty("XDR")
        private String xdr;
        @JsonProperty("XOF")
        private String xof;
        @JsonProperty("XPF")
        private String xpf;
        @JsonProperty("YER")
        private String yer;
        @JsonProperty("ZAR")
        private String zar;
        @JsonProperty("ZMK")
        private String zmk;
        @JsonProperty("ZMW")
        private String zmw;
        @JsonProperty("ZWL")
        private String zwl;
    }

}
