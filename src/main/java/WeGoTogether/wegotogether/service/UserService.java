package WeGoTogether.wegotogether.service;

import WeGoTogether.wegotogether.apiPayload.code.status.ErrorStatus;
import WeGoTogether.wegotogether.apiPayload.exception.handler.UserHandler;
import WeGoTogether.wegotogether.converter.UserConverter;
import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.repository.UserRepository;
import WeGoTogether.wegotogether.util.JwtTokenProvider;
import WeGoTogether.wegotogether.web.dto.UserRequestDTO;
import WeGoTogether.wegotogether.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public User registerUser(UserRequestDTO.signUpDTO request){

        if (!existEmail(request.getEmail())){
            throw new UserHandler(ErrorStatus.ALREADY_USE_EMAIL);
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

    // 전화번호 중복
    public boolean existPhoneNum(String phoneNum){
        Optional<User> existUser = userRepository.findByPhoneNum(phoneNum);
        return existUser.isEmpty();
    }

}
