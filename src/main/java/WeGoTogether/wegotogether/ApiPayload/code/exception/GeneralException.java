package WeGoTogether.wegotogether.ApiPayload.code.exception;

import WeGoTogether.wegotogether.ApiPayload.code.BaseErrorCode;
import WeGoTogether.wegotogether.ApiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}