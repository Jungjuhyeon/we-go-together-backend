package WeGoTogether.wegotogether.converter;

import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.security.JwtProvider;
import WeGoTogether.wegotogether.web.dto.RefreshTokenRes;
import WeGoTogether.wegotogether.web.dto.UserDtoReq;
import WeGoTogether.wegotogether.web.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

    public static RefreshTokenRes refreshTokenRes(String acceesToken){
        return RefreshTokenRes.builder()
                .accessToken(acceesToken)
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
