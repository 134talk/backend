package kr.co.auth.service;

import kr.co.user.model.User;
import kr.co.common.exception.CustomException;
import kr.co.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

        return new CustomUserDetails(user.getUserId(), user.getRole().toString());
    }
}
