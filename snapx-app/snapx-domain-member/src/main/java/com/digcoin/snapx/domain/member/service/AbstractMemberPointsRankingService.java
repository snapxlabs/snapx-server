package com.digcoin.snapx.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractMemberPointsRankingService {

    public void updateRanking(Consumer<RedisOperations> callback) {
        getStringRedisTemplate().execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                callback.accept(operations);
                return null;
            }
        });
    }

    public void updateRanking(Long memberId, LocalDateTime addTime, Long pointsAmount) {
        getStringRedisTemplate().execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                updateRanking(memberId, addTime, pointsAmount, operations);
                return null;
            }
        });
    }

    public void updateRanking(Long memberId, LocalDateTime addTime, Long pointsAmount, RedisOperations operations) {
        updateRanking(memberId, addTime, pointsAmount, false, operations);
    }

    public void updateRanking(Long memberId, LocalDateTime addTime, Long pointsAmount, boolean rest, RedisOperations operations) {
        String gamerKey = buildRankingMemberKey(addTime.toLocalDate(), memberId);
        String rankingKey = buildRankingKey(addTime.toLocalDate());
        if (rest) {
            operations.delete(gamerKey);
        }
        Long totalPoints = operations.opsForValue().increment(gamerKey, pointsAmount);
        operations.opsForZSet().add(rankingKey, String.valueOf(memberId), totalPoints);
    }

    public List<Long> listRanking(Long start, Long offset) {
        return listRanking(LocalDate.now(), start, offset);
    }

    public List<Long> listRanking(LocalDate date, Long start, Long offset) {
        String rankingKey = buildRankingKey(date);
        Set<String> range = getStringRedisTemplate().opsForZSet().reverseRange(rankingKey, start, offset);
        return range.stream().map(Long::valueOf).collect(Collectors.toList());
    }

    public Long countRanking() {
        return countRanking(LocalDate.now());
    }

    public Long countRanking(LocalDate date) {
        String rankingKey = buildRankingKey(date);
        return getStringRedisTemplate().opsForZSet().zCard(rankingKey);
    }

    public Long getRanking(Long memberId) {
        return getRanking(LocalDate.now(), memberId);
    }

    public Long getRanking(LocalDate date, Long memberId) {
        String rankingKey = buildRankingKey(date);
        Long ranking = getStringRedisTemplate().opsForZSet().reverseRank(rankingKey, String.valueOf(memberId));
        if (Objects.isNull(ranking)) {
            return null;
        }
        return ranking + 1;
    }

    public Map<Long, Long> getRankingMap(Collection<Long> memberIds) {
        return getRankingMap(LocalDate.now(), memberIds);
    }

    public Map<Long, Long> getRankingMap(LocalDate date, Collection<Long> memberIds) {
        String rankingKey = buildRankingKey(date);
        Map<Long, Long> result = new HashMap<>(memberIds.size());
        getStringRedisTemplate().execute(new SessionCallback<>() {
            @Override
            public Map<Long, Long> execute(RedisOperations operations) throws DataAccessException {
                for (Long memberId : memberIds) {
                    Long rank = operations.opsForZSet().reverseRank(rankingKey, String.valueOf(memberId));
                    result.put(memberId, rank + 1);
                }
                return null;
            }
        });
        return result;
    }

    public Map<Long, Double> getMemberScore(Collection<Long> memberIds) {
        return getMemberScore(LocalDate.now(), memberIds);
    }

    public Map<Long, Double> getMemberScore(LocalDate date, Collection<Long> memberIds) {
        String rankingKey = buildRankingKey(date);
        Map<Long, Double> result = new HashMap<>(memberIds.size());
        getStringRedisTemplate().execute(new SessionCallback<>() {
            @Override
            public Map<Long, Double> execute(RedisOperations operations) throws DataAccessException {
                for (Long memberId : memberIds) {
                    Double score = operations.opsForZSet().score(rankingKey, String.valueOf(memberId));
                    result.put(memberId, score);
                }
                return null;
            }
        });
        return result;
    }

    public void remove(LocalDate date, Long memberId) {
        String memberKey = buildRankingMemberKey(date, memberId);
        getStringRedisTemplate().delete(memberKey);
        String rankingKey = buildRankingKey(date);
        getStringRedisTemplate().opsForZSet().remove(rankingKey, String.valueOf(memberId));
    }

    protected String buildRankingMemberKey(LocalDate date, Long memberId) {
        return String.join(":", buildRankingKey(date), String.valueOf(memberId));
    }

    protected abstract String buildRankingKey(LocalDate date);

    protected abstract StringRedisTemplate getStringRedisTemplate();

}
