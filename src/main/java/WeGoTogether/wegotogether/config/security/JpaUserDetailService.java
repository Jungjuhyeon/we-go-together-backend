package WeGoTogether.wegotogether.config.security;

import WeGoTogether.wegotogether.user.entity.User;
import WeGoTogether.wegotogether.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** 스프링 시큐리티에서 UserDetails 정보를 토대로 사용자 정보를 가져오는 인터페이스
 *  Provider에서 호출하여 사용자의 정보를 가져오는데 이때 UserDetailService에는
 *  일반적으로 회원정보가 DB에 있다면 사용자의 이름으로 DB를 조회하여 비밀번호가 일치하는지 확인하여 인증을 처리
 *
 *  인증이 완료되면 UsernamePasswordAuthenticationToken에 회원 정보를 담아 UserDetail에게 리턴
 */

@Service
@RequiredArgsConstructor
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
