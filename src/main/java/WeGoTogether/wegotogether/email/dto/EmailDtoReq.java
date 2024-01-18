package WeGoTogether.wegotogether.email.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class EmailDtoReq {

    @Getter
    public static class emailAuthReq{
        @NotBlank
        private String email;
    }
}
