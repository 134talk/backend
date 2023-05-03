package kr.co.auth.jwt.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kr.co.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class JwtTokenConfig {

    private Key accessTokenKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private Key refreshTokenKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private long tokenValidTime = 1000L * 60 * 30; // 30분

    private long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 15; // 15일

    // Jwt 토큰 생성
    public String createAccessToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(date) // 토큰 발행일자
                .setExpiration(new Date(date.getTime() + tokenValidTime))
                .signWith(accessTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken() {
        String uuid = UUID.randomUUID().toString();
        Claims claims = Jwts.claims().setSubject(uuid);
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(date) // 토큰 발행일자
                .setExpiration(new Date(date.getTime() + refreshTokenValidTime))
                .signWith(refreshTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //subject 값 조회
    public String getAccessTokenSubject(String token) {
        return parseClaims(token, accessTokenKey).getSubject();
    }

    //subject 값 조회
    public String getRefreshTokenSubject(String token) {
        return parseClaims(token, refreshTokenKey).getSubject();
    }

    //accessToken 유효성 체크
    public void validAccessToken(String token) {
        try {
            parseClaims(token, accessTokenKey);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        }
    }

    //refreshToken 유효성 체크
    public void validRefreshToken(String refreshToken) {
        try {
            parseClaims(refreshToken, refreshTokenKey);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        }
    }

    private Claims parseClaims(String token, Key secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
