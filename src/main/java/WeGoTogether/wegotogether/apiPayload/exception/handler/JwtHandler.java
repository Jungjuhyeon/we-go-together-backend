package WeGoTogether.wegotogether.apiPayload.exception.handler;

import WeGoTogether.wegotogether.apiPayload.code.BaseErrorCode;
import WeGoTogether.wegotogether.apiPayload.exception.GeneralException;

public class JwtHandler extends GeneralException {
    public JwtHandler(BaseErrorCode code) {
        super(code);
    }
}
