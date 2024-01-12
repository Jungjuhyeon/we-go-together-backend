package WeGoTogether.wegotogether.web.Controller;

import WeGoTogether.wegotogether.ApiPayload.ApiResponse;
import WeGoTogether.wegotogether.converter.UserConverter;
import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.service.UserService;
import WeGoTogether.wegotogether.web.dto.RefreshTokenReq;
import WeGoTogether.wegotogether.web.dto.RefreshTokenRes;
import WeGoTogether.wegotogether.web.dto.UserDtoReq;
import WeGoTogether.wegotogether.web.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wego/users")
public class UserController {

    private final UserService userService;

    //test
    @ResponseBody
    @PostMapping("")
    public ApiResponse<UserDtoRes.userRegisterRes> signUp(@RequestBody @Valid UserDtoReq.userRegisterReq request) throws IOException {
        User user = userService.toUser(request);
        return ApiResponse.onSuccess(UserConverter.userDtoRes(user));
    }

    @ResponseBody
    @PostMapping("/login")
    public ApiResponse<UserDtoRes.userLoginRes> login(@RequestBody @Valid UserDtoReq.userLoginReq request) throws Exception {
        return ApiResponse.onSuccess(userService.login(request));
    }

   @ResponseBody
   @PostMapping("/token")
   public ApiResponse<RefreshTokenRes> login(@RequestBody RefreshTokenReq request) throws Exception {
        String accesstoken = userService.invaildToken(request);
       return ApiResponse.onSuccess(UserConverter.refreshTokenRes(accesstoken));
   }



}