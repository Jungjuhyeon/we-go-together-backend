package WeGoTogether.wegotogether.security;

import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class JpaUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        return new CustomUserDetail(user);
    }
}