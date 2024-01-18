package WeGoTogether.wegotogether.constant.Handler;

import WeGoTogether.wegotogether.constant.enums.ErrorStatus;
import WeGoTogether.wegotogether.security.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Access Denied: {}", accessDeniedException.getMessage());
       // response.sendError(HttpServletResponse.SC_FORBIDDEN);

        JwtAuthenticationFilter.setErrorResponse(response, ErrorStatus.JWT_AUTHORIZATION_FAILED); //권한이 없음.
    }
}
