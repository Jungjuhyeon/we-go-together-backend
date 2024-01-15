package WeGoTogether.wegotogether.util;

import WeGoTogether.wegotogether.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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

    private long tokenValidTime = 30 * 60 * 1000L;  // 유효시간 30분

    private final JpaUserDetailService jpaUserDetailService;

    // 객체 초기화. 비밀키를 Base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성(사용자 정보와 역할 정보를 받아와)
    public String createToken(Long userPk, String roles){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("type", "accessToken")
                .claim("userId",userPk) // 정보 저장
                .claim("roles",roles)
                .setIssuedAt(now)   // 토큰 발급 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime))  // 만료시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘 & signature에 들어갈 secret값 설정
                .compact();   // 구성한 정보를 토대로 최종적으로 JWT를 생성하고 이를 문자열로 변환하여 반환

    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        String userPk = String.valueOf(this.getUserPk(token)); //long -> string으로 형변환
        UserDetails userDetails = jpaUserDetailService.loadUserByUsername(userPk);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public Long getUserPk(String token) {
        return Long.valueOf(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("userId", Integer.class));
    }

    // Request의 Header에서 token 값을 가져옵니다. "ACCESS_TOKEN" : "TOKEN값'
    public String resolveToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("ACCESS_TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }
    }
    
}
