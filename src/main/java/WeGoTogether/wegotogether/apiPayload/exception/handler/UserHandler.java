package WeGoTogether.wegotogether.apiPayload.exception.handler;

import WeGoTogether.wegotogether.apiPayload.code.BaseErrorCode;
import WeGoTogether.wegotogether.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}