package kr.co.talk.domain.social;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    /**
     * 로그인 완료시 응답 객체
     * */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginResponseDto {
        private String userUid;
        private String accessToken;
//        private String refreshToken;
    }

}
