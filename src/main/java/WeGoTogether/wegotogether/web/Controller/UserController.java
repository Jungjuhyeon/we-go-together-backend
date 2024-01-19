package WeGoTogether.wegotogether.web.Controller;

import WeGoTogether.wegotogether.ApiPayload.ApiResponse;
import WeGoTogether.wegotogether.converter.UserConverter;
import WeGoTogether.wegotogether.domain.User;

import WeGoTogether.wegotogether.service.UserService;
import WeGoTogether.wegotogether.service.UserServiceImpl;
import WeGoTogether.wegotogether.web.dto.RefreshTokenReq;
import WeGoTogether.wegotogether.web.dto.RefreshTokenRes;
import WeGoTogether.wegotogether.web.dto.UserReqDTO;
import WeGoTogether.wegotogether.web.dto.UserResDTO;
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


    //test
    @ResponseBody
    @PostMapping("/signUp")
    public ApiResponse<UserResDTO.userSignUpRes> signUp(@RequestBody @Valid UserReqDTO.userSignUpReq requestDTO) throws Exception {
        User user = userService.toSignUp(requestDTO);
        return ApiResponse.onSuccess(UserConverter.userDtoRes(user));
    }



}