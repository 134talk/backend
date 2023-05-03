package kr.co.talk.global.service;

import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * get value
     * @param key
     * @return
     */
    public String getValues(String key) {
        ValueOperations<String, String> values = stringRedisTemplate.opsForValue();
        return values.get(key);
    }

    /**
     * set value with timeout
     * 
     * @param key
     * @param value
     * @param timeout
     */
    public void setValuesWithTimeout(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMillis(timeout));
    }
}
