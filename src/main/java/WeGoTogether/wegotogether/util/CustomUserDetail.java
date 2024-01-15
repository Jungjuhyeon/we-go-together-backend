package WeGoTogether.wegotogether.util;

import WeGoTogether.wegotogether.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import lombok.*;

@RequiredArgsConstructor
@Getter
public class CustomUserDetail implements UserDetails {

    private final User user;


    // 사용자가 가지는 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
    }

    // 사용자의 id 반환
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // 사용자의 passwqrd 반환
    @Override
    public String getPassword(){
        return user.getPassword();
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;  // true는 만료되지 않음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;  // true는 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // true는 만료되지 않음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true;  // true는 사용 가능
    }
}
