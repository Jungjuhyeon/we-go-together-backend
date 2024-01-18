package WeGoTogether.wegotogether.member.Controller;

import WeGoTogether.wegotogether.constant.ApiResponse;
import WeGoTogether.wegotogether.member.converter.UserConverter;
import WeGoTogether.wegotogether.member.model.User;
import WeGoTogether.wegotogether.security.JwtProvider;
import WeGoTogether.wegotogether.member.service.UserServiceImpl;
import WeGoTogether.wegotogether.member.dto.RefreshTokenRes;
import WeGoTogether.wegotogether.member.dto.UserDtoReq;
import WeGoTogether.wegotogether.member.dto.UserDtoRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/wego/users")
public class UserController {

    private final UserServiceImpl userService;
    private final JwtProvider jwtProvider;

    //test
    @ResponseBody
    @PostMapping("")
    public ApiResponse<UserDtoRes.userRegisterRes> signUp(@RequestBody @Valid UserDtoReq.userRegisterReq request) {
        User user = userService.toUser(request);
        return ApiResponse.onSuccess(UserConverter.userDtoRes(user));
    }

    @ResponseBody
    @PostMapping("/login")
    public ApiResponse<UserDtoRes.userLoginRes> login(@RequestBody @Valid UserDtoReq.userLoginReq request) {
        return ApiResponse.onSuccess(userService.login(request));
    }

   @PostMapping("/token")
   public ApiResponse<RefreshTokenRes> login(@RequestHeader("Authorization") String refreshToken) {
        String accessToken = userService.inVaildToken(refreshToken);
       return ApiResponse.onSuccess(UserConverter.refreshTokenRes(accessToken));
   }

    @ResponseBody
    @PatchMapping("/password-restore")
    public ApiResponse<UserDtoRes.passwordRestoreRes> signUp(@RequestBody @Valid UserDtoReq.passwordRestoreReq request){
        Long userId= jwtProvider.getUserID();
        User user = userService.pwRestore(request,userId);
        return ApiResponse.onSuccess(UserConverter.passwordRestoreRes(user));
    }

}