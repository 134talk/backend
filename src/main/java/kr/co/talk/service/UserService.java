package kr.co.talk.service;

import kr.co.talk.domain.user.User;
import kr.co.talk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.talk.domain.social.SocialKakaoDto.UserInfoDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void createUser(UserInfoDto userInfoDto) {
        User user = userInfoDto.createUser();
        userRepository.save(user);
    }

    public boolean existUser(String id) {
        User user = userRepository.findByUserUid(id);
        if (user == null) {
            return false;
        }
        return true;
    }
}
