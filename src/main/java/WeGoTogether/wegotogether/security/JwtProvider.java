package WeGoTogether.wegotogether.security;


import WeGoTogether.wegotogether.domain.Enum.UserState;
import WeGoTogether.wegotogether.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

// 토큰을 생성하고 검증하는 클래스입니다.
// 해당 컴포넌트는 필터클래스에서 사전 검증을 거칩니다.
@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    // 토큰 유효시간 30분
    private long tokenValidTime = 1000*60*30;

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
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT refresh 토큰 생성
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "refreshToken")
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime*60*24*7)) // set Expire Time
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
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.get("userId", String.class));
    }

    // Request의 Header에서 token 값을 가져옵니다. "ACCESS-TOKEN" : "TOKEN값'
    public String resolveAcceessToken(HttpServletRequest request) {
        return request.getHeader("ACCESS-TOKEN");
    }

    // Request의 Header에서 token 값을 가져옵니다. "REFRESH-TOKEN" : "TOKEN값'
    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader("REFRESH-TOKEN");
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




}
