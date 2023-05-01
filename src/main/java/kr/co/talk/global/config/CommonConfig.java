package kr.co.talk.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 공통으로 사용되는 빈 정의
 * */
@Configuration
public class CommonConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
