package WeGoTogether.wegotogether.apiPayload.code.status;

import WeGoTogether.wegotogether.apiPayload.code.BaseErrorCode;
import WeGoTogether.wegotogether.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

public enum ErrorStatus implements BaseErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    ALREADY_USE_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER4002", "이미 사용중인 이메일입니다."),
    ALREADY_USE_PHONENUMBER(HttpStatus.BAD_REQUEST, "MEMBER4003", "이미 사용중인 번호입니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),
    FOOD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FOOD_CATEGORY4001", "음식 카테고리가 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE4001", "가게를 찾을 수 없습니다."),
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION4001", "미션을 찾을 수 없습니다."),
    MISSION_ALREADY_DONE(HttpStatus.BAD_REQUEST, "MISSION4002", "이미 미션을 진행하였습니다."),
    NO_PAGE_REQUEST(HttpStatus.NOT_FOUND, "PAGE4001", "없는 페이지 입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4001", "없는 카테고리입니다."),

    //JWT Error
    JWT_EMPTY(HttpStatus.UNAUTHORIZED,"JWT4100","JWT 토큰을 넣어주세요."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED,"JWT4101","다시 로그인 해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder().message(this.message).code(this.code).isSuccess(false).build();
    }

    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder().message(this.message).code(this.code).isSuccess(false).httpStatus(this.httpStatus).build();
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    private ErrorStatus(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}

