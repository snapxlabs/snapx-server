package com.digcoin.snapx.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberPointsHistoryRankingService extends AbstractMemberPointsRankingService {

    private static final String KEY_PREFIX = "SNAPX:MEMBER:POINTS:RANKING:HISTORY";

    private final StringRedisTemplate redisTemplate;

    @Override
    protected String buildRankingKey(LocalDate date) {
        return KEY_PREFIX;
    }

    @Override
    protected StringRedisTemplate getStringRedisTemplate() {
        return redisTemplate;
    }

}
