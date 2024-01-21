package WeGoTogether.wegotogether.config.security;

import WeGoTogether.wegotogether.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

/**
 * 토큰을 생성하고 검증하는 클래스
 * 해당 컴포넌트는 필터 클래스에서 사전 검증을 거침
 * */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret_key}")
    private String secretKey;

    private long tokenValidTime = 60 * 60 * 1000L;  // 유효시간 60분

    private final JpaUserDetailService jpaUserDetailService;
    private final UserRepository userRepository;

    // 객체 초기화. 비밀키를 Base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성(사용자 정보와 역할 정보를 받아와)
    public String createToken(String email, String roles){
        Date now = new Date();

        return Jwts.builder()
                .setSubject(email)
                .claim("email",email) // 정보 저장
                .claim("roles",roles)
                .setIssuedAt(now)   // 토큰 발급 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime))  // 만료시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘 & signature에 들어갈 secret값 설정
                .compact();   // 구성한 정보를 토대로 최종적으로 JWT를 생성하고 이를 문자열로 변환하여 반환
    }

    // refreshToken 생성
     public String refreshToken(String email){
         Date now = new Date();

         return Jwts.builder()
                 .claim("email",email) // 정보 저장
                 .setIssuedAt(now)   // 토큰 발급 시간 정보
                 .setExpiration(new Date(now.getTime() + tokenValidTime+(1000*60*60*24*7)))  // 만료시간 설정(일주일)
                 .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘 & signature에 들어갈 secret값 설정
                 .compact();   // 구성한 정보를 토대로 최종적으로 JWT를 생성하고 이를 문자열로 변환하여 반환
     }

    // Spring Security에서 권한 확인을 위한 기능
    // 사용자가 제공한 토큰을 기반으로 사용자의 권한 정보를 얻어오는 역할
    public Authentication getAuthentication(String token) {
        String userPk = this.getUserEmail(token);
        UserDetails userDetails = jpaUserDetailService.loadUserByUsername(userPk);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨 있는 회원 이메일 정보 추출
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        /**
         * .parseClaimsJws(token) : 토큰을 파싱하여 클레임을 얻고
         * .getBody().getSubject() 를 통해 subject에 해당하는 클레임을 가져와 사용자 이메일을 반환
         */
    }

    // Request의 Header에서 token 값을 가져옵니다. "authorization" : "token'
    public String resolveToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        if(request.getHeader("authorization") != null )
            // "Bearer" 부분을 제외하고 토큰 값만 추출
            return request.getHeader("authorization").substring(7);
        return null;
    }

    public String resolveRefreshToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        if(request.getHeader("refreshToken") != null )
            return request.getHeader("refreshToken").substring(7);
        return null;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {    // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            log.info(e.getMessage());
            return false;
        }
    }

    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("authorization", "bearer "+ accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "bearer "+ refreshToken);
    }

    // Email로 권한 정보 가져오기
    public String getRoles(String email) {
        return userRepository.findByEmail(email).get().getRole().name();
    }


    // 리프레시 토큰 존재 여부
    public boolean existRefreshToken(String token){
        return userRepository.findByRefreshToken(token).isPresent();
    }
    
}
