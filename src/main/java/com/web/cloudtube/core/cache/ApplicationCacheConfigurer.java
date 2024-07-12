package com.web.cloudtube.core.cache;

import com.web.cloudtube.core.cache.delegator.CacheTaskDelegator;
import com.web.cloudtube.core.cache.delegator.LettuceTaskDelegator;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@ConfigurationProperties("spring.data.redis")
@PropertySource("classpath:bootstrap.yml")
public class ApplicationCacheConfigurer {

    @Value("${host}")
    private String redisHostname;

    @Value("${port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30); // maximal connection number
        config.setMaxIdle(10);  // maximal idle connection
        config.setMaxWait(Duration.ofSeconds(10)); // maximal waiting time in seconds
        return config;
    }

    @Bean
    public StatefulRedisConnection<String, String> redisConnection() {
        RedisURI redisUri = RedisURI.builder()
                .withHost(redisHostname)
                .withPort(redisPort)
                .withDatabase(0)
                .withTimeout(Duration.of(30, ChronoUnit.SECONDS))
                .build();
        RedisClient redisClient = RedisClient
                .create(redisUri);
        return redisClient.connect();
    }

    @Bean
    public CacheTaskDelegator cacheTaskDelegator(StatefulRedisConnection<String, String> redisConnection) {
        return new LettuceTaskDelegator(redisConnection);
    }
}
