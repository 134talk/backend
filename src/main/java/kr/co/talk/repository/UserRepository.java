package kr.co.talk.repository;

import kr.co.talk.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserUid(String userUid);
}
