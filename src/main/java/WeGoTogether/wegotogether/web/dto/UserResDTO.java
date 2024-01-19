package WeGoTogether.wegotogether.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class UserResDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userSignUpRes{
        public Long userId;
        public LocalDateTime createdAt;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userLoginRes{
        private Long userId;
        private String accessToken;
        private String refreshToken;
        private LocalDateTime createdAt;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class passwordRestoreRes{
        private Long userId;
        private LocalDateTime createdAt;
    }

}