package com.digcoin.snapx.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberPointsDailyRankingService extends AbstractMemberPointsRankingService {

    private static final String KEY_PREFIX = "SNAPX:MEMBER:POINTS:RANKING:DAILY";

    private final StringRedisTemplate redisTemplate;

    @Override
    protected String buildRankingKey(LocalDate date) {
        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());
        String day = String.valueOf(date.getDayOfMonth());
        return String.join(":", KEY_PREFIX, year, month, day);
    }

    @Override
    protected StringRedisTemplate getStringRedisTemplate() {
        return redisTemplate;
    }

}
