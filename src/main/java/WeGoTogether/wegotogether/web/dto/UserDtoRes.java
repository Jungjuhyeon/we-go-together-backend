package WeGoTogether.wegotogether.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class UserDtoRes {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userRegisterRes{

        public Long userId;
        public LocalDateTime createdAt;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userLoginRes{

        public Long userId;
        public String accessToken;
        public String refreshToken;
        public LocalDateTime createdAt;
    }
}
