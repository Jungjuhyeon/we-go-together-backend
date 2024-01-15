package WeGoTogether.wegotogether.security;

import WeGoTogether.wegotogether.ApiPayload.code.exception.Handler.JwtHandler;
import WeGoTogether.wegotogether.ApiPayload.code.status.ErrorStatus;
import WeGoTogether.wegotogether.repository.UserRepository;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;
import java.util.Date;

// 토큰을 생성하고 검증하는 클래스입니다.
// 해당 컴포넌트는 필터클래스에서 사전 검증을 거칩니다.
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    // 토큰 유효시간 30분
    public static final long TOKEN_VALID_TIME = 1000L * 60 * 5 * 5; // 5분
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 144; // 일주일
    public static final long REFRESH_TOKEN_VALID_TIME_IN_REDIS = 60 * 60 * 24 * 7; // 일주일 (초)

    private final JpaUserDetailsService jpaUserDetailsService;
    private final UserRepository userRepository;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT access 토큰 생성
    public String createAccessToken(Long userPk, String roles) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "accessToken")
                .claim("userId",userPk) // 정보 저장
                .claim("roles",roles)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT refresh 토큰 생성
    public String createRefreshToken(Long userPk) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "refreshToken")
                .claim("userId",userPk) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보(권한) 조회
    public Authentication getAuthentication(String token) {
        String userPk = String.valueOf(this.getUserPk(token)); //long -> string으로 형변환

        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(userPk);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 userid 추출
    public Long getUserPk(String token) {
        //if(!validateToken(token)){ //만료됐으면 그냥 에러 던져!
        //    throw new JwtHandler(ErrorStatus.JWT_EXPIRED);
        //}
        //이는 정수형이므로 long으로 변환하여 반환
        return Long.valueOf(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userId", Integer.class));
    }


    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
    public String resolveAcceessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserID(){
        String token = resolveAcceessToken();
        return getUserPk(token);
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

}