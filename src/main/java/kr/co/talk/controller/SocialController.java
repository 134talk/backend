package kr.co.talk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.talk.service.SocialKakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static kr.co.talk.domain.social.SocialKakaoDto.*;

@RestController
@RequiredArgsConstructor
public class SocialController {
    private final SocialKakaoService socialKakaoService;

    @PostMapping("/login/kakao")
    public ResponseEntity loginKakao(@RequestBody TokenRequestDto loginDto) throws Exception {
        return new ResponseEntity(socialKakaoService.login(loginDto),HttpStatus.OK);

    }
    
}
