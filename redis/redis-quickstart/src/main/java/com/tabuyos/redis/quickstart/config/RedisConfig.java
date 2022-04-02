/*
 * Copyright (c) 2018-2022 Tabuyos All Right Reserved.
 */
package com.tabuyos.redis.quickstart.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * RedisConfig
 *
 * @author tabuyos
 * @since 2022/4/2
 */
@Configuration
@EnableCaching
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    // 使用Jackson2JsonRedisSerialize 替换默认序列化
    // Jackson序列化  json占用的内存最小
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getValueSerializer();
    // Jdk序列化   JdkSerializationRedisSerializer是最高效的
    // JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
    // String序列化
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    // key采用String的序列化方式
    redisTemplate.setKeySerializer(stringRedisSerializer);
    // hash的key也采用String的序列化方式
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    // value序列化方式采用jackson
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    // hash的value序列化方式采用jackson
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  private Jackson2JsonRedisSerializer<Object> getValueSerializer() {
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    return jackson2JsonRedisSerializer;
  }

  /**
   * springboot cache 生成key过于简单，容易冲突userCache::3
   * <p>
   * 自定义KeyGenerator 使用时必须 @Cacheable(keyGenerator = "simpleKeyGenerator")
   *
   * @return key 生成器
   */
  @Bean
  public KeyGenerator simpleKeyGenerator() {
    return (o, method, objects) -> {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(o.getClass().getSimpleName());
      stringBuilder.append(".");
      stringBuilder.append(method.getName());
      stringBuilder.append("[");
      for (Object obj : objects) {
        stringBuilder.append(obj.toString());
      }
      stringBuilder.append("]");
      return stringBuilder.toString();
    };
  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    return new RedisCacheManager(
      RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
      // 默认策略，未配置的 key 会使用这个
      this.getRedisCacheConfigurationWithTtl(600),
      // 指定 key 策略
      this.getRedisCacheConfigurationMap()
    );
  }

  private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
    Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
    redisCacheConfigurationMap.put("UserInfoList", this.getRedisCacheConfigurationWithTtl(100));
    redisCacheConfigurationMap.put("UserInfoListAnother", this.getRedisCacheConfigurationWithTtl(18000));
    return redisCacheConfigurationMap;
  }

  private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
    redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                                                       RedisSerializationContext
                                                         .SerializationPair
                                                         .fromSerializer(getValueSerializer()))
                                                     .entryTtl(Duration.ofSeconds(seconds));
    return redisCacheConfiguration;
  }
}
