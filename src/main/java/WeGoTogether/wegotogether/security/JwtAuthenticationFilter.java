package WeGoTogether.wegotogether.security;

import WeGoTogether.wegotogether.ApiPayload.code.exception.Handler.JwtHandler;
import WeGoTogether.wegotogether.ApiPayload.code.status.ErrorStatus;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtProvider.resolveAcceessToken();


        if (!token.isEmpty()) {
            try {
                jwtProvider.parseToken(token); //파싱 유효성 검사
                if (!request.getRequestURI().equals("/wego/users/token")) {
                    String isLogout = redisUtil.getData(token);
                    // getData 해서 값이 가져와지면 AT가 블랙리스트에 등록된 상태이므로 로그아웃된 상태임.
                    if (isLogout == null) {
                        Authentication authentication = jwtProvider.getAuthentication(token);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (ExpiredJwtException e) {
                log.error("Enter [EXPIRED TOKEN]");
                throw new JwtHandler(ErrorStatus.JWT_EXPIRED);
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException |
                     IllegalArgumentException e) {
                log.error("Enter [INVALID TOKEN]");
                throw new JwtHandler(ErrorStatus.JWT_BAD);
            }
        } else {
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
