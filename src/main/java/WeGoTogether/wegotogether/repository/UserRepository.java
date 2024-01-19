package WeGoTogether.wegotogether.repository;

import WeGoTogether.wegotogether.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long usePk);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNum(String phoneNum);

    boolean existsByEmail(String email);

    boolean existsByPhoneNum(String phoneNum);

}