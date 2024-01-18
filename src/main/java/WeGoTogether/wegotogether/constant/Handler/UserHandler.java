package WeGoTogether.wegotogether.constant.Handler;

import WeGoTogether.wegotogether.constant.code.BaseErrorCode;
import WeGoTogether.wegotogether.constant.exception.CustomException;

public class UserHandler extends CustomException {

    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}