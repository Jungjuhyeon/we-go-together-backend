package WeGoTogether.wegotogether.service;


import WeGoTogether.wegotogether.domain.User;
import WeGoTogether.wegotogether.web.dto.UserReqDTO;

public interface UserService  {
        public User toSignUp(UserReqDTO.userSignUpReq requestDTO) throws Exception;
    }
