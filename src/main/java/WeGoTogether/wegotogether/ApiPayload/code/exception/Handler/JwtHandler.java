package WeGoTogether.wegotogether.ApiPayload.code.exception.Handler;

import WeGoTogether.wegotogether.ApiPayload.code.BaseErrorCode;
import WeGoTogether.wegotogether.ApiPayload.code.exception.GeneralException;

public class JwtHandler extends GeneralException {

    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}