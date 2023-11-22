package com.digcoin.snapx.domain.infra.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.digcoin.snapx.core.common.error.CommonError;
import com.digcoin.snapx.core.error.BusinessException;
import com.digcoin.snapx.core.mybatis.PageResult;
import com.digcoin.snapx.core.mybatis.helper.PageHelper;
import com.digcoin.snapx.domain.infra.bo.CountryBO;
import com.digcoin.snapx.domain.infra.bo.PhoneAreaCodeQuery;
import com.digcoin.snapx.domain.infra.entity.PhoneAreaCode;
import com.digcoin.snapx.domain.infra.mapper.PhoneAreaCodeMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneAreaCodeService implements ApplicationRunner {

    private static final String URL_STRING = "https://raw.githubusercontent.com/dr5hn/countries-states-cities-database/master/countries.json";

    private final PhoneAreaCodeMapper phoneAreaCodeMapper;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        log.info("start pull  Country data from {}", URL_STRING);
//        updatePhoneAreaCode();
//        log.info("finish pulling Country data");
    }

    public PageResult<PhoneAreaCode> pagePhoneAreaCode(PhoneAreaCodeQuery query) {
        IPage<PhoneAreaCode> page = phoneAreaCodeMapper.selectPage(PageHelper.getPage(query),
                Wrappers.lambdaQuery(PhoneAreaCode.class)
                        .like(StringUtils.isNotBlank(query.getSearch()), PhoneAreaCode::getMix, query.getSearch())
                        .in(CollectionUtils.isNotEmpty(query.getCountryCodes()), PhoneAreaCode::getCountryCode, query.getCountryCodes())
                        .orderByAsc(PhoneAreaCode::getId));
        return PageResult.fromPage(page, Function.identity());
    }

    public PhoneAreaCode getPhoneAreaCode(String countryCode) {
        return phoneAreaCodeMapper.selectOne(Wrappers.lambdaQuery(PhoneAreaCode.class)
                .eq(PhoneAreaCode::getCountryCode, countryCode));
    }

    private void updatePhoneAreaCode() {
        jdbcTemplate.execute("truncate `inf_phone_area_code`");
        List<CountryBO> phoneAreaCodeBOList = listCountry();
        for (CountryBO bo : phoneAreaCodeBOList) {
            PhoneAreaCode entity = new PhoneAreaCode();
            entity.setId(Long.valueOf(bo.getId()));
            entity.setEnglishName(bo.getName());
            entity.setCountryCode(bo.getIso2());
            entity.setChineseName(bo.getTranslations().getCn());
            entity.setPhoneCode(bo.getPhoneCode());
            entity.setEmoji(bo.getEmoji());
            entity.setMix(entity.getEnglishName() + entity.getChineseName() + entity.getCountryCode() + entity.getPhoneCode());
            phoneAreaCodeMapper.insert(entity);
        }
    }

    private List<CountryBO> listCountry() {
        try {
            byte[] raw = getFromGithubRaw();
            return objectMapper.readValue(raw, new TypeReference<>() {
            });
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("listPhoneAreaCode", e);
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }

    private byte[] getFromGithubRaw() {
        try {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));
            URL url = new URL(URL_STRING);
            URLConnection connection = url.openConnection(proxy);
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            IOUtils.copy(inputStream, outputStream);
            return outputStream.toByteArray();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("getFromGithubRaw", e);
            throw CommonError.UNEXPECT_ERROR.withDefaults();
        }
    }

}
