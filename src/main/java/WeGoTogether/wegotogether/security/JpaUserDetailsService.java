package WeGoTogether.wegotogether.security;

import WeGoTogether.wegotogether.member.model.User;
import WeGoTogether.wegotogether.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk)  {

        User user = userRepository.findById(Long.parseLong(userPk)).orElseThrow(
                () -> new UsernameNotFoundException("")
        );
        return new CustomUserDetail(user);	// 위에서 생성한 CustomUserDetails Class
    }
}


