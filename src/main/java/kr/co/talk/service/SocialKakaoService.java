package kr.co.talk.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.talk.exception.CustomException;
import kr.co.talk.global.config.JwtTokenConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static kr.co.talk.domain.social.SocialKakaoDto.*;
import static kr.co.talk.domain.user.LoginDto.LoginResponseDto;


@Service
@Slf4j
@RequiredArgsConstructor
public class SocialKakaoService {

    private final UserService userService;
    private final JwtTokenConfig jwtTokenConfig;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;


    public LoginResponseDto login(TokenRequestDto requestDto) throws Exception {

        //액세슨 토큰 발급
        TokenResponseDto tokenResponseDto = getAccessToken(requestDto);

        //액세스 토큰으로 유저 정보 조회
        UserInfoDto userInfo = getUserInfo(tokenResponseDto.getAccess_token());

        //유저 정보 없으면 유저 생성 먼저 진행
        if(userService.existUser(userInfo.getId())){
            userService.createUser(userInfo);
        }

        //jwt 토큰 발급
        String token = jwtTokenConfig.createJwtToken(userInfo.getId());

        return new LoginResponseDto(userInfo.getId(),token);

    }

    /**
     * 액세스 토큰 조회
     * **/

    private TokenResponseDto getAccessToken(TokenRequestDto requestDto) throws Exception {

        String url = "https://kauth.kakao.com/oauth/token";

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
            responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(paramMap, headers),
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

        String url = "https://kapi.kakao.com/v2/user/me";

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        //param
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

        ResponseEntity<String> responseEntity = null;

        //http 요청
        try {
            responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(paramMap, headers),
                    String.class);
        } catch (HttpClientErrorException e) {
            throw new CustomException(e.getMessage(),e.getStatusCode().value());
        }
        return objectMapper.readValue(responseEntity.getBody(), UserInfoDto.class);
    }
}
