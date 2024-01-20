package WeGoTogether.wegotogether.user.dto;

import lombok.*;

import java.time.LocalDateTime;

public class UserResponseDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupResDTO{

        private Long userId;
        private LocalDateTime createdAt;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResDTO{
        Long userId;
        String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindIdResDTO{
        String userId;
    }
    
}
