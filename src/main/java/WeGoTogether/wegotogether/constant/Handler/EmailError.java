package WeGoTogether.wegotogether.constant.Handler;

import WeGoTogether.wegotogether.constant.code.BaseErrorCode;
import WeGoTogether.wegotogether.constant.exception.CustomException;

public class EmailError extends CustomException {

    public EmailError(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
