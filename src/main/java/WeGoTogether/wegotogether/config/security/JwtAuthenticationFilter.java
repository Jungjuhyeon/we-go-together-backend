package WeGoTogether.wegotogether.config.security;

import WeGoTogether.wegotogether.apiPayload.code.status.ErrorStatus;
import WeGoTogether.wegotogether.apiPayload.exception.handler.JwtHandler;
import WeGoTogether.wegotogether.apiPayload.exception.handler.UserHandler;
import WeGoTogether.wegotogether.user.converter.UserConverter;
import WeGoTogether.wegotogether.user.dto.TokenResponseDTO;
import WeGoTogether.wegotogether.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Jwt가 유효성을 검증하는 필터

/**
 * 필터에서 해당 요청을 낚아채서 요청을 검증하여 토큰을 생성한다.
 * OncePerRequestFilter는 단 한번의 요청에 단 한번만 동작하도록 보장된 필터
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    //private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = jwtTokenProvider.resolveToken();

        // accessToken 검사.
        if (accessToken != null) {
            // 토큰이 유효하다면 토큰으로부터 유저 정보를 받아옴
            if (jwtTokenProvider.validateToken(accessToken)){
                Authentication auth = jwtTokenProvider.getAuthentication(accessToken);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            // 어세스 토큰이 만료 & 리프레시 토큰 또한 존재하는 상황
            //else if (!jwtTokenProvider.validateToken(accessToken)) {    // 재발급 후, 컨텍스트에 다시 넣기
            else {
                String refreshToken = jwtTokenProvider.resolveRefreshToken();

                // refreshToken 존재유무 확인
                boolean isRefreshToken = jwtTokenProvider.existRefreshToken(refreshToken);
                // refreshToken 검증
                boolean validateRefreshToken = jwtTokenProvider.validateToken(refreshToken);

                if (validateRefreshToken && isRefreshToken) {
                    // 리프레시 토큰으로 이메일 정보 가져오기
                    String email = jwtTokenProvider.getUserEmail(refreshToken);
                    // 이메일로 권한 정보 받아오기
                    String roles = jwtTokenProvider.getRoles(email);
                    // 토큰 발급
                    String newAccessToken = jwtTokenProvider.createToken(email, roles);
                    // 헤더에 어세스 토큰 추가
                    jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);

                    // 새로 발급된 AccessToken을 응답으로 전달
                    UserConverter.newAccessTokenRes(newAccessToken);

                    // 컨텍스트에 넣기
                    Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                else if ((!validateRefreshToken) && isRefreshToken){  // 리프레시 토큰 만료
                    throw new JwtHandler(ErrorStatus.REFRESH_TOKEN_INVALID);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}