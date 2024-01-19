package WeGoTogether.wegotogether.web.dto;

import lombok.*;

@Builder
@Setter
@Getter
public class RefreshTokenRes {
    private String accessToken;
}