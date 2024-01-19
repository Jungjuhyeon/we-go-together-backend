package WeGoTogether.wegotogether.web.dto;

import WeGoTogether.wegotogether.validation.annotation.ExistPhone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserReqDTO {
    //회원가입
    @Setter
    @Getter
    public static class userSignUpReq{

        String email;
        @NotBlank
        private String password;
        @ExistPhone
        String PhoneNum;

    }
    //로그인
    @Getter
    public static class userLoginReq{
        @Email
        private String email;
        @NotBlank
        private String password;
    }
    //비밀번호
    @Getter
    @Setter
    public static class passwordRestoreReq{
        @NotBlank
        private String password;
        @NotBlank
        private String passwordCheck;

        @NotBlank
        private String newPassword;


    }

}