package kr.co.talk.service;

import kr.co.talk.domain.user.User;
import kr.co.talk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.talk.domain.social.SocialKakaoDto.UserInfoDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User createUser(UserInfoDto userInfoDto) {
        User user = userInfoDto.createUser();
        return userRepository.save(user);
    }

    public User findByUserUid(String userUid) {
        return userRepository.findByUserUid(userUid);
    }

}
