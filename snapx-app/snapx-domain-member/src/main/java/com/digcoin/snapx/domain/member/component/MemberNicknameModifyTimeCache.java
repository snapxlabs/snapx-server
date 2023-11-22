package com.digcoin.snapx.domain.member.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberNicknameModifyTimeCache {

    private static final String KEY_PREFIX = "MEM:MEMBER:NICKNAME:MODIFY";
    private final StringRedisTemplate redisTemplate;

    public LocalDateTime getLastModifyTime(Long memberId) {
        String datetime = redisTemplate.opsForValue().get(buildKey(memberId));
        if (StringUtils.isBlank(datetime)) {
            return null;
        }
        return LocalDateTime.parse(datetime, DateTimeFormatter.ISO_DATE_TIME);
    }

    public void updateModifyTime(Long memberId, LocalDateTime modifyTime) {
        String datetime = DateTimeFormatter.ISO_DATE_TIME.format(modifyTime);
        String key = buildKey(memberId);
        redisTemplate.opsForValue().set(key, datetime, 30, TimeUnit.DAYS);
    }

    private String buildKey(Long memberId) {
        return String.join(":", KEY_PREFIX, String.valueOf(memberId));
    }
}
