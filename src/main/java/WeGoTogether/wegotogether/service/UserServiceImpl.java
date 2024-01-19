package WeGoTogether.wegotogether.service;

import WeGoTogether.wegotogether.converter.UserConverter;
import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.repository.UserRepository;
import WeGoTogether.wegotogether.web.dto.UserReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public User toSignUp(UserReqDTO.userSignUpReq requestDTO) throws Exception {
        //중복처리
        if(!existEmail(requestDTO.getEmail())){
            throw new Exception("이미 존재하는 이메일 입니다.");
        }

        if(!existPhoneNum(requestDTO.getPhoneNum())){
            throw new Exception("이미 존재하는 번호입니다.");
        }
        requestDTO.setPassword(bCryptPasswordEncoder.encode(requestDTO.getPassword())); // 비밀번호 암호화

        User user = UserConverter.toUser(requestDTO);


        return userRepository.save(user);
    }

    //중복 처리
    public boolean existEmail(String email){
        Optional<User> existUser = userRepository.findByEmail(email);
        return existUser.isEmpty();
    }
    public boolean existPhoneNum(String phoneNum){
        Optional<User> existUser = userRepository.findByPhoneNum(phoneNum);
        return existUser.isEmpty();
    }



}
