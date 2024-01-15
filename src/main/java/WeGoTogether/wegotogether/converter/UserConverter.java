package WeGoTogether.wegotogether.converter;

import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.web.dto.UserRequestDTO;
import WeGoTogether.wegotogether.web.dto.UserResponseDTO;

import java.time.LocalDateTime;

public class UserConverter {

    public static User toUser(UserRequestDTO.signUpDTO request){
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNum(request.getPhoneNum())
                .build();
    }

    // 회원가입
    public static UserResponseDTO.SignupResDTO signupRes(User user){
        return UserResponseDTO.SignupResDTO.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
