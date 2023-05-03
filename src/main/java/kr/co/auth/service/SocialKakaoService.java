package kr.co.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.user.model.User;
import kr.co.common.exception.CustomException;
import kr.co.auth.jwt.config.JwtTokenConfig;
import kr.co.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static kr.co.user.dto.SocialKakaoDto.*;
import static kr.co.user.dto.LoginDto.LoginResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialKakaoService {

    private final UserService userService;
    private final JwtTokenConfig jwtTokenConfig;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.kakao.url.token}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.kakao.url.profile}")
    private String profileUrl;

    public LoginResponseDto login(TokenRequestDto requestDto) throws Exception {

        //액세슨 토큰 발급
        TokenResponseDto tokenResponseDto = getAccessToken(requestDto);

        //액세스 토큰으로 유저 정보 조회
        UserInfoDto userInfo = getUserInfo(tokenResponseDto.getAccess_token());

        //유저 정보 없으면 유저 생성 먼저 진행
        User user = userService.findByUserUid(userInfo.getId());
        if(user == null){
            user = userService.createUser(userInfo);
        }

        //jwt 토큰 발급
        //accesstoken userId로 발급
        String accessToken = jwtTokenConfig.createAccessToken(String.valueOf(user.getUserId()));
        String refreshToken = jwtTokenConfig.createRefreshToken();

        return new LoginResponseDto(user.getUserId(),accessToken,refreshToken);

    }

    /**
     * 액세스 토큰 조회
     * **/
    private TokenResponseDto getAccessToken(TokenRequestDto requestDto) throws Exception {
        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //param
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("client_id", requestDto.getClientId());
        paramMap.add("redirect_uri", requestDto.getRedirectUrl());
        paramMap.add("code", requestDto.getCode());
        paramMap.add("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = null;

        //http 요청
        try {
            responseEntity = restTemplate.postForEntity(tokenUrl, new HttpEntity<>(paramMap, headers),
                    String.class);
        } catch (HttpClientErrorException e) {
            throw new CustomException(e.getMessage(),e.getStatusCode().value());
        }

        return objectMapper.readValue(responseEntity.getBody(), TokenResponseDto.class);

    }

    /**
     * 액세스 토큰으로 유저 정보 조회
     * **/
    private UserInfoDto getUserInfo(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        //param
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

        ResponseEntity<String> responseEntity = null;

        //http 요청
        try {
            responseEntity = restTemplate.postForEntity(profileUrl, new HttpEntity<>(paramMap, headers),
                    String.class);
        } catch (HttpClientErrorException e) {
            throw new CustomException(e.getMessage(),e.getStatusCode().value());
        }
        return objectMapper.readValue(responseEntity.getBody(), UserInfoDto.class);
    }
}
