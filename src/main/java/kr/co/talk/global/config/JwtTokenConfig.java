package kr.co.talk.global.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenConfig {

    //    private String secretKey = "";
    private Key secretKey = Keys.hmacShaKeyFor("".getBytes());

    private long tokenValidTime = 1000L * 60 * 30; // 30분만 토큰 유효

    private long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 30; // 30일

    // Jwt 토큰 생성
    public String createJwtToken(String userUid) {
        Claims claims = Jwts.claims().setSubject(userUid);
//        Keys.secretKeyFor(SignatureAlgorithm.HS512);
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(date) // 토큰 발행일자
                .setExpiration(new Date(date.getTime() + tokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //subject 값 조회
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    //token 유효성 체크
    public void validToken(String token) {
        try {
            parseClaims(token);
        } catch (Exception e) {
            throw e;

        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
