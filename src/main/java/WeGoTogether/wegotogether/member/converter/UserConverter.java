package WeGoTogether.wegotogether.member.converter;

import WeGoTogether.wegotogether.member.dto.RefreshTokenRes;
import WeGoTogether.wegotogether.member.dto.UserDtoReq;
import WeGoTogether.wegotogether.member.dto.UserDtoRes;
import WeGoTogether.wegotogether.member.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Slf4j
@Component
@RequiredArgsConstructor
public class UserConverter {


    //회원가입 응답
    public static UserDtoRes.userRegisterRes userDtoRes(User user){
        return UserDtoRes.userRegisterRes.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //로그인 응답
    public static UserDtoRes.userLoginRes userLoginRes(User user, String accessToken, String refreshToken){
        return UserDtoRes.userLoginRes.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .build();
    }

    //accessToken 재발급 응답
    public static RefreshTokenRes refreshTokenRes(String acceesToken){
        return RefreshTokenRes.builder()
                .accessToken(acceesToken)
                .build();
    }

    //passwordRestore 응답
    public static UserDtoRes.passwordRestoreRes passwordRestoreRes(User user){
        return UserDtoRes.passwordRestoreRes.builder()
                .userId(user.getId())
                .createdAt(user.getCreatedAt())
                .build();
    }

    //유저 객체 만들기
    public static User toUser(UserDtoReq.userRegisterReq request){
        return User.builder()
                .email(request.getEmail())
                .pw(request.getPassword())
                .phoneNum(request.getPhoneNum())
                .build();
    }
}
