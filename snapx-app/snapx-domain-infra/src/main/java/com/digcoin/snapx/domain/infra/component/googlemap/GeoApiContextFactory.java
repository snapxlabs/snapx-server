package com.digcoin.snapx.domain.infra.component.googlemap;

import com.digcoin.snapx.core.advice.log.RequestIdUtil;
import com.digcoin.snapx.domain.infra.config.properties.GoogleMapsGeoProperties;
import com.digcoin.snapx.domain.infra.config.properties.VpnProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.OkHttpRequestHandler;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author simonmao
 * @version 1.0.0
 * @date 2023/4/16 09:22
 * @description
 */
@Slf4j
public class GeoApiContextFactory {

    public static final String LANGUAGE_EN_US = "en-US";
    public static final String LANGUAGE_ZH_HK = "zh-HK";
    public static final String LANGUAGE_DEFAULT = LANGUAGE_EN_US;

    private GoogleMapsGeoProperties googleMapsGeoProperties;
    private VpnProperties vpnProperties;
    private ObjectMapper objectMapper;
    private Map<String, GeoApiContext> geoApiContextMap = new HashMap<>(16);

    public void setGoogleMapsGeoProperties(GoogleMapsGeoProperties googleMapsGeoProperties) {
        this.googleMapsGeoProperties = googleMapsGeoProperties;
    }

    public void setVpnProperties(VpnProperties vpnProperties) {
        this.vpnProperties = vpnProperties;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GeoApiContext get(String language) {
        GeoApiContext context = geoApiContextMap.get(language);
        if (Objects.nonNull(context)) {
            return context;
        }

        context = create(language);
        geoApiContextMap.put(language, context);
        return context;
    }

    public GeoApiContext getDefault() {
        return get(LANGUAGE_DEFAULT);
    }

    public GeoApiContext create(String language) {
        GeoApiContext.Builder builder = new GeoApiContext.Builder();
        builder.apiKey(googleMapsGeoProperties.getApiKey());

        OkHttpRequestHandler.Builder okHttpBuilder = new OkHttpRequestHandler.Builder();
        okHttpBuilder.okHttpClientBuilder().addInterceptor(chain -> {
            RequestIdUtil.setRequestIdIfAbsent();

            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder().addHeader("Accept-Language", language);
            Request newRequest = requestBuilder.build();
            log.info("Google Maps - Request API: method={}, url={}, headers={}", newRequest.method(), newRequest.url(), objectMapper.writeValueAsString(newRequest.headers()));
            return chain.proceed(newRequest);
        });
        builder.requestHandlerBuilder(okHttpBuilder);

        if (Objects.nonNull(vpnProperties) && Objects.equals(vpnProperties.getEnabled(), true)) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(vpnProperties.getHostname(), vpnProperties.getPort()));
            builder.proxy(proxy);
        }

        return builder.build();
    }

}
