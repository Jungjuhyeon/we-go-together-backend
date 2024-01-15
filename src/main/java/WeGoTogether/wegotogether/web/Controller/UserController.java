package WeGoTogether.wegotogether.web.Controller;

import WeGoTogether.wegotogether.apiPayload.ApiResponse;
import WeGoTogether.wegotogether.converter.UserConverter;
import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.service.UserService;
import WeGoTogether.wegotogether.web.dto.UserRequestDTO;
import WeGoTogether.wegotogether.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ApiResponse<UserResponseDTO.SignupResDTO> signup(@RequestBody @Valid UserRequestDTO.signUpDTO request){
        User user = userService.registerUser(request);
        return ApiResponse.onSuccess(UserConverter.signupRes(user));
    }


}
