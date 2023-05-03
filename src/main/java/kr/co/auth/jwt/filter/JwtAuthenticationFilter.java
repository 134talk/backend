package kr.co.auth.jwt.filter;

import kr.co.auth.jwt.config.JwtTokenConfig;
import kr.co.auth.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenConfig jwtTokenConfig;
    private final CustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //header에서 jwt 토큰 확인
        String authorization = request.getHeader("Authorization");

        //인증 정보 없을 때
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //Bearer 붙지 않았을 때
        if (!authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split("Bearer ")[1].trim();

        //인증 정보 저장
        setAuthentication(token);

    }


    private void setAuthentication(String token) {

        //토근 유효성 통과 안됐을시 예외 발생
        jwtTokenConfig.validAccessToken(token);

        //subject를 조회 : userId
        String userId = jwtTokenConfig.getAccessTokenSubject(token);

        //userDetails 조회
        UserDetails userDetails = getUserDetails(userId);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        //시큐리티 컨텍스트에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private UserDetails getUserDetails(String userId) {
        return userDetailService.loadUserByUsername(userId);
    }
}
