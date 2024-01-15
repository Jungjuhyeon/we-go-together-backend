package WeGoTogether.wegotogether.web.dto;

import WeGoTogether.wegotogether.validation.annotation.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


public class UserDtoReq {

    @Setter
    @Getter
    public static class userRegisterReq{

        @Email
        private String email;
        @NotBlank
        private String password;
        @Phone
        private String phoneNum;

    }
    @Getter
    public static class userLoginReq{
        @Email
        private String email;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public static class passwordRestoreReq{
        @NotBlank
        private String password;
        @NotBlank
        private String passwordCheck;
    }
}
