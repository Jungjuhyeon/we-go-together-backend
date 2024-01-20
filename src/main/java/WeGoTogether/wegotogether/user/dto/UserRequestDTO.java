package WeGoTogether.wegotogether.user.dto;

import WeGoTogether.wegotogether.user.validation.annotation.ExistPhone;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class signUpDTO{
        @Email
        String email;
        @NotBlank
        String userId;
        @NotBlank
        String password;
        @ExistPhone
        String phoneNum;
    }

    @Getter
    @Setter
    public static class loginDTO{
        @Email
        String email;
        @NotBlank
        String password;
    }

    @Getter
    @Setter
    public static class findIdDTO{
        @Email
        String email;
    }
}
