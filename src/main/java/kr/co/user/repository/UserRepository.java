package kr.co.user.repository;

import kr.co.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserUid(String userUid);
}
