package WeGoTogether.wegotogether.user.dto;

import lombok.*;

import java.time.LocalDateTime;

public class TokenResponseDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class newAccessTokenRes{
        Long userId;
        String accessToken;
        String refreshToken;
    }
}
