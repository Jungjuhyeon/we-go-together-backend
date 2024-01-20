package WeGoTogether.wegotogether.user.service;

import WeGoTogether.wegotogether.apiPayload.ApiResponse;
import WeGoTogether.wegotogether.apiPayload.code.status.ErrorStatus;
import WeGoTogether.wegotogether.apiPayload.exception.handler.JwtHandler;
import WeGoTogether.wegotogether.apiPayload.exception.handler.UserHandler;
import WeGoTogether.wegotogether.user.converter.UserConverter;
import WeGoTogether.wegotogether.user.dto.UserRequestDTO;
import WeGoTogether.wegotogether.user.dto.UserResponseDTO;
import WeGoTogether.wegotogether.user.entity.User;
import WeGoTogether.wegotogether.user.repository.UserRepository;
import WeGoTogether.wegotogether.config.security.JpaUserDetailService;
import WeGoTogether.wegotogether.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MailService mailService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider; // JwtTokenProvider는 기존에 설명한 JWT 토큰 생성 및 검증을 담당하는 클래스
    private final JpaUserDetailService jpaUserDetailService;   // JpaUserDetailService는 Spring Security의 UserDetailsService를 구현한 클래스로, 사용자 정보를 가져오는 역할

    // 회원가입
    public User registerUser(UserRequestDTO.signUpDTO request){

        if (!existEmail(request.getEmail())){
            throw new UserHandler(ErrorStatus.ALREADY_USE_EMAIL);
        }

        if (!existUserID(request.getUserId())){
            throw new UserHandler(ErrorStatus.ALREADY_USE_ID);
        }

        if (!existPhoneNum(request.getPhoneNum())){
            throw new UserHandler(ErrorStatus.ALREADY_USE_PHONENUMBER);
        }

        request.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));  // 비밀번호 암호화

        User newUser = UserConverter.toUser(request);

        return userRepository.save(newUser);
    }

    // 이메일 중복
    public boolean existEmail(String email){
        Optional<User> existUser = userRepository.findByEmail(email);
        return existUser.isEmpty();
    }

    // 아이디 중복
    public boolean existUserID(String userId) {
        Optional<User> existUserId = userRepository.findByUserId(userId);
        return existUserId.isEmpty();
    }

    // 전화번호 중복
    public boolean existPhoneNum(String phoneNum){
        Optional<User> existUser = userRepository.findByPhoneNum(phoneNum);
        return existUser.isEmpty();
    }

    public User validUserByEmail(String email)  {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    // 아이디 찾기
    public String returnUserId(String email, String verifiedCode){
        User user = validUserByEmail(email); // 입력된 이메일 존재 확인 후 객체 생성;

        if (isValidVerificationCode(user) && verifiedCode.equals(user.getVerifiedCode())){
            // 인증번호가 일치하고 유효하다면 사용자 아이디 반환
            return user.getUserId();
        }
        else if(isValidVerificationCode(user) && !verifiedCode.equals(user.getVerifiedCode())){
            throw new UserHandler(ErrorStatus.NOT_INVALID_CODE);
        }
        else{
            throw new UserHandler(ErrorStatus.EXPIRE_CODE);
        }
    }

    // 인증번호 만료 여부
    private boolean isValidVerificationCode(User user) {
        LocalDateTime expirationTime = user.getVerificationCodeExpiration();
        return LocalDateTime.now().isBefore(expirationTime);
    }

}
