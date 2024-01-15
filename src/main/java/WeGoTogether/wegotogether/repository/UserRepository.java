package WeGoTogether.wegotogether.repository;

import WeGoTogether.wegotogether.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long userPk);
    Optional<User> findByEmail(String email);   // email로 사용자 정보를 가져옴
    Optional<User> findByPhoneNum(String phoneNum);
}
