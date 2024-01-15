package WeGoTogether.wegotogether.ApiPayload.code.status;

import WeGoTogether.wegotogether.ApiPayload.code.BaseErrorCode;
import WeGoTogether.wegotogether.ApiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // User Error

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "사용자가 없습니다."),
    USER_ID_PASSWORD_FOUND(HttpStatus.BAD_REQUEST, "USER4002", "아이디 또는 비밀번호가 잘못되었습니다."),
    USER_PASSWORD_ERROR(HttpStatus.BAD_REQUEST, "USER4003", "패스워드를 확인하세요.(최소8자,최대20자,영문자,숫자 모두 포함되어야 합니다."),
    USER_EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "USER4004", "이미 등록된 이메일 입니다."),
    USER_PHONE_DUPLICATE(HttpStatus.BAD_REQUEST, "USER4005", "이미 등록된 휴대폰 번호 입니다."),
    USER_PASSWORD_NONEQULE(HttpStatus.BAD_REQUEST, "USER4006", "비밀번호가 일치하지 않습니다."),
    USER_PASSWORD_EXIST(HttpStatus.BAD_REQUEST, "USER4007", "이전 비밀번호와 일치합니다."),



    //JWT Error
    JWT_EMPTY(HttpStatus.UNAUTHORIZED,"JWT4100","JWT 토큰을 넣어주세요."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED,"JWT4101","다시 로그인 해주세요."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,"JWT4102","토큰이 만료되었습니다."),
    JWT_BAD(HttpStatus.UNAUTHORIZED,"JWT4102","JWT 토큰이 잘못되었습니다."),
    JWT_REFRESHTOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED,"JWT4103","RefreshToken이 일치하지 않습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
