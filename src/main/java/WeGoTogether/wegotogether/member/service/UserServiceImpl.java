package WeGoTogether.wegotogether.member.service;

import WeGoTogether.wegotogether.constant.Handler.JwtHandler;
import WeGoTogether.wegotogether.constant.Handler.UserHandler;
import WeGoTogether.wegotogether.constant.enums.ErrorStatus;
import WeGoTogether.wegotogether.member.converter.UserConverter;
import WeGoTogether.wegotogether.member.model.User;
import WeGoTogether.wegotogether.member.repository.UserRepository;
import WeGoTogether.wegotogether.security.JwtProvider;
import WeGoTogether.wegotogether.security.RedisUtil;
import WeGoTogether.wegotogether.member.dto.UserDtoReq;
import WeGoTogether.wegotogether.member.dto.UserDtoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final JwtProvider jwtProvider;
    public final RedisUtil redisUtil;

    @Override
    public User toUser(UserDtoReq.userRegisterReq request){

        // 이메일 형식 확인
        validateEmail(request.getEmail());

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserHandler(ErrorStatus.USER_EMAIL_DUPLICATE);
        }

        // 휴대폰 형식 확인
        validatePhone(request.getPhoneNum());

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

    @Override
    public UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request) {

        //해당 Email로 아이디 찾기 - 아이디 불일치
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_ID_PASSWORD_FOUND));

        //비밀번호 불일치
        if (!passwordEncoder.matches(request.getPassword(), user.getPw())) {
            throw new UserHandler(ErrorStatus.USER_ID_PASSWORD_FOUND);
        }

        //토큰 생성
        String accessToken = jwtProvider.createAccessToken(user.getId(),user.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // RefreshToken을 redis에 저장
        redisUtil.setDataExpire("RT:" + user.getId(), refreshToken, jwtProvider.REFRESH_TOKEN_VALID_TIME_IN_REDIS);

        return UserConverter.userLoginRes(user,accessToken,refreshToken);
    }

    @Override
    public String inVaildToken(String refreshToken) {;

        //pk 추출
        Long userId = jwtProvider.getUserPkInToken(refreshToken);
        //pk로 아이디 찾기
        User findUser = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //redis에서 userid를 기준으로 가져오기
        String refreshTokenInRedis = redisUtil.getData("RT:" + userId);

        //토큰이 일치하지 않을떄
        if(!refreshToken.equals(refreshTokenInRedis)) {
            throw new JwtHandler(ErrorStatus.JWT_REFRESHTOKEN_NOT_MATCH);
        }

        String newAccessToken = jwtProvider.createAccessToken(userId,findUser.getRole().name());

        return newAccessToken;
    }

    @Override
    //패스워드 재설정
    public User pwRestore(UserDtoReq.passwordRestoreReq request,Long userId){
        //유저 아이디 찾기(유저 객체 조회)
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        //비밀번호 정규화 확인
        validatePassword(request.getPassword());
        //비밀번호 일치한지 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_NONEQULE);
        }

        //기존 비밀번호와 일치한지 확인
        if(passwordEncoder.matches(request.getPassword(), user.pw)) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_EXIST);
        }

        //비밀번호 암호화 (저장)
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPw(request.getPassword());
        userRepository.save(user);
        return user;
    }


    // 비밀번호 정규식 확인 함수
    private void validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new UserHandler(ErrorStatus.USER_PASSWORD_ERROR);
        }
    }

    // 이메일 정규식 확인 함수
    private void validateEmail(String email){
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()){
            throw new UserHandler(ErrorStatus.USER_EMAIL_ERROR);
        }
    }

    // 휴대폰 번호 점규식 확인 함수
    private void validatePhone(String phone){
        Pattern pattern = Pattern.compile("^01(?:0|1|[6-9])-(\\d{3}|\\d{4})-\\d{4}$");
        Matcher matcher = pattern.matcher(phone);
        if (!matcher.matches()){
            throw new UserHandler(ErrorStatus.USER_PHONE_ERROR);
        }
    }

}
