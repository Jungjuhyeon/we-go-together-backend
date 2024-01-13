package WeGoTogether.wegotogether.web.Controller;

import WeGoTogether.wegotogether.ApiPayload.ApiResponse;
import WeGoTogether.wegotogether.converter.UserConverter;
import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.security.JwtProvider;
import WeGoTogether.wegotogether.service.UserService;
import WeGoTogether.wegotogether.web.dto.RefreshTokenReq;
import WeGoTogether.wegotogether.web.dto.RefreshTokenRes;
import WeGoTogether.wegotogether.web.dto.UserDtoReq;
import WeGoTogether.wegotogether.web.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/wego/users")
public class UserController {

    private final UserService userService;
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

   @ResponseBody
   @PostMapping("/token")
   public ApiResponse<RefreshTokenRes> login(@RequestBody RefreshTokenReq request){
        String accesstoken = userService.invaildToken(request);
       return ApiResponse.onSuccess(UserConverter.refreshTokenRes(accesstoken));
   }

    @ResponseBody
    @PatchMapping("/password-restore")
    public ApiResponse<UserDtoRes.passwordRestoreRes> signUp(@RequestBody @Valid UserDtoReq.passwordRestoreReq request){
        Long userId= jwtProvider.getUserID();
        User user = userService.pwRestore(request,userId);
        return ApiResponse.onSuccess(UserConverter.passwordRestoreRes(user));
    }

}