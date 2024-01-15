package WeGoTogether.wegotogether.web.dto;

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

}
