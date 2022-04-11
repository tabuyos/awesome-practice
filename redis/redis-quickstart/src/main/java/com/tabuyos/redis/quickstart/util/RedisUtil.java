/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.redis.quickstart.util;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisUtil
 *
 * @author tabuyos
 * @since 2022/4/2
 */
@SuppressWarnings("unused")
@Service
public class RedisUtil {

  private final RedisTemplate<String, Object> redisTemplate;
  private static final double size = Math.pow(2, 32);

  public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 写入缓存
   *
   * @param key    key
   * @param offset 位 8Bit=1Byte
   * @return boolean
   */
  public boolean setBit(String key, long offset, boolean isShow) {
    boolean result = false;
    try {
      ValueOperations<String, Object> operations = redisTemplate.opsForValue();
      operations.setBit(key, offset, isShow);
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 写入缓存
   *
   * @param key    key
   * @param offset offset
   * @return boolean
   */
  public boolean getBit(String key, long offset) {
    boolean result = false;
    try {
      ValueOperations<String, Object> operations = redisTemplate.opsForValue();
      result = Optional.ofNullable(operations.getBit(key, offset)).orElse(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 写入缓存
   *
   * @param key   key
   * @param value value
   * @return boolean
   */
  public boolean set(final String key, Object value) {
    boolean result = false;
    try {
      ValueOperations<String, Object> operations = redisTemplate.opsForValue();
      operations.set(key, value);
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 写入缓存设置时效时间
   *
   * @param key   key
   * @param value value
   * @return boolean
   */
  public boolean set(final String key, Object value, Long expireTime) {
    boolean result = false;
    try {
      ValueOperations<String, Object> operations = redisTemplate.opsForValue();
      operations.set(key, value);
      redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 批量删除对应的value
   *
   * @param keys keys
   */
  public void remove(final String... keys) {
    for (String key : keys) {
      remove(key);
    }
  }

  /**
   * 删除对应的value
   *
   * @param key key
   */
  public void remove(final String key) {
    if (exists(key)) {
      redisTemplate.delete(key);
    }
  }

  /**
   * 判断缓存中是否有对应的value
   *
   * @param key key
   * @return boolean
   */
  public boolean exists(final String key) {
    return Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
  }

  /**
   * 读取缓存
   *
   * @param key key
   * @return object
   */
  public Object get(final String key) {
    Object result;
    ValueOperations<String, Object> operations = redisTemplate.opsForValue();
    result = operations.get(key);
    return result;
  }

  /**
   * 读取缓存
   *
   * @param key key
   * @return object
   */
  public Object genValue(final String key) {
    Object result;
    ValueOperations<String, Object> operations = redisTemplate.opsForValue();
    result = operations.get(key);
    return result;
  }

  /**
   * 哈希 添加
   *
   * @param key     key
   * @param hashKey hash key
   * @param value   value
   */
  public void hmSet(String key, Object hashKey, Object value) {
    HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    hash.put(key, hashKey, value);
  }

  /**
   * 哈希获取数据
   *
   * @param key     key
   * @param hashKey hash key
   * @return object
   */
  public Object hmGet(String key, Object hashKey) {
    HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    return hash.get(key, hashKey);
  }

  /**
   * 列表添加
   *
   * @param key   key
   * @param value value
   */
  public void lPush(String key, Object value) {
    ListOperations<String, Object> list = redisTemplate.opsForList();
    list.leftPush(key, value);
  }

  /**
   * 列表添加
   *
   * @param key            key
   * @param timeoutSeconds timeout seconds
   */
  public Object rPop(String key, long timeoutSeconds) {
    try {
      return redisTemplate.opsForList().rightPop(key, timeoutSeconds, TimeUnit.SECONDS);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 列表获取
   *
   * @param key   key
   * @param start start
   * @param end   end
   * @return list
   */
  public List<Object> lRange(String key, long start, long end) {
    ListOperations<String, Object> list = redisTemplate.opsForList();
    return list.range(key, start, end);
  }

  /**
   * 集合添加
   *
   * @param key   key
   * @param value value
   */
  public void add(String key, Object value) {
    SetOperations<String, Object> set = redisTemplate.opsForSet();
    set.add(key, value);
  }

  /**
   * 集合获取
   *
   * @param key key
   * @return set
   */
  public Set<Object> setMembers(String key) {
    SetOperations<String, Object> set = redisTemplate.opsForSet();
    return set.members(key);
  }

  /**
   * 有序集合添加
   *
   * @param key   key
   * @param value value
   * @param score score
   */
  public void zAdd(String key, Object value, double score) {
    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    zset.add(key, value, score);
  }

  /**
   * 有序集合获取
   *
   * @param key      key
   * @param minScore min score
   * @param maxScore max score
   * @return set
   */
  public Set<Object> rangeByScore(String key, double minScore, double maxScore) {
    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    redisTemplate.opsForValue();
    return zset.rangeByScore(key, minScore, maxScore);
  }


  /**
   * 第一次加载的时候将数据加载到redis中
   *
   * @param name name
   */
  public void saveDataToRedis(String name) {
    double index = Math.abs(name.hashCode() % size);
    long indexLong = Double.valueOf(index).longValue();
    boolean availableUsers = setBit("availableUsers", indexLong, true);
  }

  /**
   * 第一次加载的时候将数据加载到redis中
   *
   * @param name name
   * @return boolean
   */
  public boolean getDataToRedis(String name) {

    double index = Math.abs(name.hashCode() % size);
    long indexLong = Double.valueOf(index).longValue();
    return getBit("availableUsers", indexLong);
  }

  /**
   * 有序集合获取排名
   *
   * @param key   集合名称
   * @param value 值
   */
  public Long zRank(String key, Object value) {
    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    return zset.rank(key, value);
  }


  /**
   * 有序集合获取排名
   *
   * @param key key
   */
  public Set<ZSetOperations.TypedTuple<Object>> zRankWithScore(String key, long start, long end) {
    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    return zset.rangeWithScores(key, start, end);
  }

  /**
   * 有序集合添加
   *
   * @param key   key
   * @param value value
   */
  public Double zSetScore(String key, Object value) {
    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    return zset.score(key, value);
  }


  /**
   * 有序集合添加分数
   *
   * @param key   key
   * @param value value
   * @param score score
   */
  public void incrementScore(String key, Object value, double score) {
    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    zset.incrementScore(key, value, score);
  }


  /**
   * 有序集合获取排名
   *
   * @param key key
   */
  public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithScore(String key, long start, long end) {
    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    return zset.reverseRangeByScoreWithScores(key, start, end);
  }

  /**
   * 有序集合获取排名
   *
   * @param key key
   */
  public Set<ZSetOperations.TypedTuple<Object>> reverseZRankWithRank(String key, long start, long end) {
    ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
    return zset.reverseRangeWithScores(key, start, end);
  }
}
