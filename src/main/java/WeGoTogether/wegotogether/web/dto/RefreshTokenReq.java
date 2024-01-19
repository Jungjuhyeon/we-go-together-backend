package WeGoTogether.wegotogether.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RefreshTokenReq {
    private String accessToken;
    private String refreshToken;
}