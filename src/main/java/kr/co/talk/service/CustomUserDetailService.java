package kr.co.talk.service;

import kr.co.talk.domain.user.User;
import kr.co.talk.exception.CustomException;
import kr.co.talk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()->
                new CustomException("유저가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED.value()));
        if (user == null) {
            return null;
        }

        return new CustomUserDetails(user.getUserId(), user.getRole());
    }
}
