package WeGoTogether.wegotogether.member.repository;

import WeGoTogether.wegotogether.member.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long usePk);

    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);

    boolean existsByPhoneNum(String phoneNum);

}

