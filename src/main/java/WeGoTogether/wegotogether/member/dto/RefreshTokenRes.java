package WeGoTogether.wegotogether.member.dto;

import lombok.*;

@Builder
@Setter
@Getter
public class RefreshTokenRes {
    private String accessToken;
}
