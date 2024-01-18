package WeGoTogether.wegotogether.member.service;

import WeGoTogether.wegotogether.email.dto.EmailDtoReq;
import WeGoTogether.wegotogether.member.dto.UserDtoReq;
import WeGoTogether.wegotogether.member.dto.UserDtoRes;
import WeGoTogether.wegotogether.member.model.User;

public interface UserService {

    User signUp(UserDtoReq.userRegisterReq request);

    UserDtoRes.userLoginRes login(UserDtoReq.userLoginReq request);

    String inVaildToken(String refreshToken);

    User pwRestore(UserDtoReq.passwordRestoreReq request,Long userId);

    void sendEmailAuth(EmailDtoReq.emailAuthReq request);

    void verifyEmail(String certificationNumber, String email);
}
