package WeGoTogether.wegotogether.ApiPayload.code.exception.Handler;

import WeGoTogether.wegotogether.ApiPayload.code.BaseErrorCode;
import WeGoTogether.wegotogether.ApiPayload.code.exception.CustomException;

public class UserHandler extends CustomException {

    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}