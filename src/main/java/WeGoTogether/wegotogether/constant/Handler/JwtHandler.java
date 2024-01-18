package WeGoTogether.wegotogether.constant.Handler;

import WeGoTogether.wegotogether.constant.code.BaseErrorCode;
import WeGoTogether.wegotogether.constant.exception.CustomException;

public class JwtHandler extends CustomException {

    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}