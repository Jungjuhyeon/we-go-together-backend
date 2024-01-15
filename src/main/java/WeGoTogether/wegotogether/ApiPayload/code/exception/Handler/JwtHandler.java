package WeGoTogether.wegotogether.ApiPayload.code.exception.Handler;

import WeGoTogether.wegotogether.ApiPayload.code.BaseErrorCode;
import WeGoTogether.wegotogether.ApiPayload.code.exception.CustomException;

public class JwtHandler extends CustomException {

    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}