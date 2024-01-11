package WeGoTogether.wegotogether.web.dto;

import WeGoTogether.wegotogether.validation.annotation.Phone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDtoReq {

    @Setter
    @Getter
    public static class userRegisterReq{

        @Email
        public String email;
        @NotBlank
        public String password;
        @Phone
        public String phoneNum;

    }

    @Getter
    public static class userLoginReq{
        @Email
        public String email;
        @NotBlank
        public String password;

    }
}
