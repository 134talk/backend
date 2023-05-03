package kr.co.talk.global.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    @DisplayName("Redis set value with timeout test")
    void setValuesWithTimeoutTest() {
        final String key = "test_key";
        final String value = "test_value";
        redisService.setValuesWithTimeout(key, value, 3000);

        assertNotNull(redisService.getValues(key));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertNull(redisService.getValues(key));
    }

}
