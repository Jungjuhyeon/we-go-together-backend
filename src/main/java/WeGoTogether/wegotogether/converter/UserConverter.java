package WeGoTogether.wegotogether.converter;

import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.repository.UserRepository;
import WeGoTogether.wegotogether.web.dto.RefreshTokenRes;
import WeGoTogether.wegotogether.web.dto.UserReqDTO;
import WeGoTogether.wegotogether.web.dto.UserResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Slf4j
@Component
@RequiredArgsConstructor
public class UserConverter {


    //회원가입 응답
    public static UserResDTO.userSignUpRes userDtoRes(User user){
        return UserResDTO.userSignUpRes.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
    //회원가입
    public static User toUser(UserReqDTO.userSignUpReq requestDTO) {
        return User.builder()
                .email(requestDTO.getEmail())
                .pw(requestDTO.getPassword())
                .phoneNum((requestDTO.getPhoneNum()))
                .build();
    }

    //로그인 응답
    public static UserResDTO.userLoginRes userLoginRes(User user, String accessToken, String refreshToken){
        return UserResDTO.userLoginRes.builder()
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
    public static UserResDTO.passwordRestoreRes passwordRestoreRes(User user){
        return UserResDTO.passwordRestoreRes.builder()
                .userId(user.getId())
                .createdAt(user.getCreatedAt())
                .build();
    }



}