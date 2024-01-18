package WeGoTogether.wegotogether.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "비회원"),
    CUSTOMER("ROLE_USER", "회원자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}
