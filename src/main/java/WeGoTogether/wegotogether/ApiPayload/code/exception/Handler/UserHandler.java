package WeGoTogether.wegotogether.ApiPayload.code.exception.Handler;

import WeGoTogether.wegotogether.ApiPayload.code.BaseErrorCode;
import  WeGoTogether.wegotogether.ApiPayload.code.exception.GeneralException;

public class UserHandler extends GeneralException{

    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}