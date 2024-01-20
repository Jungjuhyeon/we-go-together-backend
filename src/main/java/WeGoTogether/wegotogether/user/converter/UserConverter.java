package WeGoTogether.wegotogether.user.converter;

import WeGoTogether.wegotogether.user.dto.TokenResponseDTO;
import WeGoTogether.wegotogether.user.dto.UserRequestDTO;
import WeGoTogether.wegotogether.user.dto.UserResponseDTO;
import WeGoTogether.wegotogether.user.entity.User;

import java.time.LocalDateTime;

public class UserConverter {

    public static User toUser(UserRequestDTO.signUpDTO request){
        return User.builder()
                .email(request.getEmail())
                .userId(request.getUserId())  // 아이디
                .password(request.getPassword())  // 비밀번호
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

    // 로그인
    public static UserResponseDTO.LoginResDTO loginRes(User user, String accessToken){
        return UserResponseDTO.LoginResDTO.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .build();
    }

    // accessToken 재발급
    public static TokenResponseDTO.newAccessTokenRes newAccessTokenRes(String accessToken){
        return TokenResponseDTO.newAccessTokenRes.builder()
                .accessToken(accessToken)
                .build();
    }

}
