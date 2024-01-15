package WeGoTogether.wegotogether.web.dto;

import WeGoTogether.wegotogether.validation.annotation.ExistPhone;
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
        String password;
        @ExistPhone
        String phoneNum;
    }

}
