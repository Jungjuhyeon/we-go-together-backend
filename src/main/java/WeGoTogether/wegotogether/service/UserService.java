package WeGoTogether.wegotogether.service;

import WeGoTogether.wegotogether.ApiPayload.code.exception.Handler.JwtHandler;
import WeGoTogether.wegotogether.ApiPayload.code.exception.Handler.UserHandler;
import WeGoTogether.wegotogether.ApiPayload.code.status.ErrorStatus;
import WeGoTogether.wegotogether.converter.UserConverter;
import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.repository.UserRepository;
import WeGoTogether.wegotogether.security.JwtProvider;
import WeGoTogether.wegotogether.web.dto.RefreshTokenReq;
import WeGoTogether.wegotogether.web.dto.RefreshTokenRes;
import WeGoTogether.wegotogether.web.dto.UserDtoReq;
import WeGoTogether.wegotogether.web.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Transactional
@Service
@RequiredArgsConstructor
public class UserService  {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final JwtProvider jwtProvider;

    public User toUser(UserDtoReq.userRegisterReq request){

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserHandler(ErrorStatus.USER_EMAIL_DUPLICATE);
        }

        // 폰 번호 중복 확인
        if (userRepository.existsByPhoneNum(request.getPhoneNum())) {
            throw new UserHandler(ErrorStatus.USER_PHONE_DUPLICATE);
        }

        //비밀번호 정규화 확인
        validatePassword(request.getPassword());
        //비밀번호 암호화
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        User newUser = UserConverter.toUser(request);

        return userRepository.save(newUser);
    }

    // 비밀번호 정규식 확인 함수
    private void validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_ERROR);
        }
    }

    public UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request) throws Exception{

        //해당 Email로 아이디 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //비밀번호 불일치
        if (!passwordEncoder.matches(request.getPassword(), user.getPw())) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        //토큰 생성
        String accessToken = jwtProvider.createAccessToken(user.getId(), "USER");
        String refreshToken = jwtProvider.createRefreshToken();

        // RefreshToken을 사용자 엔터티에 저장
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return UserConverter.userLoginRes(user,accessToken,refreshToken);
    }

    //refresh토큰 유효성 검사 + access token 재발급
    public String invaildToken(RefreshTokenReq request){
        String refreshToken = request.getRefreshToken();

        Long userId = jwtProvider.getUserPk(request.getAccessToken());
        User user  = userRepository.findById(userId) .orElseThrow(() ->  new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // refreshtoken 유효성검사
        if(jwtProvider.validateToken(user.getRefreshToken())){

            String accessToken = jwtProvider.createAccessToken(userId,"USER");

            return accessToken;
        }
        else{
            throw new JwtHandler(ErrorStatus.JWT_INVALID);
        }
    }





}
