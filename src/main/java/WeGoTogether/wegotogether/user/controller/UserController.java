package WeGoTogether.wegotogether.user.controller;

import WeGoTogether.wegotogether.apiPayload.ApiResponse;
import WeGoTogether.wegotogether.apiPayload.code.status.ErrorStatus;
import WeGoTogether.wegotogether.apiPayload.code.status.SuccessStatus;
import WeGoTogether.wegotogether.apiPayload.exception.handler.UserHandler;
import WeGoTogether.wegotogether.config.security.JwtTokenProvider;
import WeGoTogether.wegotogether.user.converter.UserConverter;
import WeGoTogether.wegotogether.user.dto.UserRequestDTO;
import WeGoTogether.wegotogether.user.dto.UserResponseDTO;
import WeGoTogether.wegotogether.user.entity.User;
import WeGoTogether.wegotogether.user.repository.UserRepository;
import WeGoTogether.wegotogether.user.service.MailService;
import WeGoTogether.wegotogether.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signUp")
    public ApiResponse<UserResponseDTO.SignupResDTO> signup(@RequestBody @Valid UserRequestDTO.signUpDTO request){
        User user = userService.registerUser(request);
        return ApiResponse.onSuccess(UserConverter.signupRes(user));
    }

    @GetMapping("/login")
    public ApiResponse<UserResponseDTO.LoginResDTO> login(@RequestBody @Valid UserRequestDTO.loginDTO request, HttpServletResponse response){

        User user = userService.validUserByEmail(request.getEmail());  // 해당 이메일을 가진 아이디가 있을 때 가져와 User 객체에 저장.

        // 입력된 비밀번호와 DB에 암호화되어 저장된 비밀번호가 다르다면
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())){
            throw  new UserHandler(ErrorStatus.PASSWORD_MISS);   // 에러 메시지 전달
        }

        // 유효한 비밀번호라면 accessToken, refreshToken 발급
        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtTokenProvider.refreshToken(user.getEmail());

        // 헤더 설정
        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return ApiResponse.onSuccess(UserConverter.loginRes(user, accessToken));
    }

    // 이메일 인증 -> 인증코드 발급
    @PostMapping("/email")
    public ApiResponse<String> findId(@RequestBody @Valid UserRequestDTO.findIdDTO request){

        User user = userService.validUserByEmail(request.getEmail());
        String code = mailService.sendMail(request.getEmail());

        user.setVerifiedCode(code);
        userRepository.save(user);

        return ApiResponse.onSuccess(code);
    }

    // 인증코드 확인 -> 아이디 찾기
    @GetMapping("/verified-code")
    public ApiResponse<String> verifiedCode(@RequestParam String email, @RequestParam String code){
        String userId = userService.returnUserId(email, code);

        return ApiResponse.onSuccess(userId);
    }

}
