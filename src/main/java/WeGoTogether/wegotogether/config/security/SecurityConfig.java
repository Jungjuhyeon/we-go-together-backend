package WeGoTogether.wegotogether.config.security;

import WeGoTogether.wegotogether.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 스프링 시큐리티는 각각의 역할에 맞는 작업을 처리하는 여러개의 필터들이
 * 체인 형태로 구성되어 순서에 따라 순차적으로 수행
 * 그 중 UsernamePasswordAuthenticationFilter가 인증처리를 담당
 */

@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter{


    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /** .antMatchers()는 해당 URL로 요청 시 설정을 해준다.
         *  authenticated()는 antMatchers에 속해 있는 URL로 요청이 어면 인증이 필요하다고 설정
         *  .hasRole()은 antMatchers에 속해 있는 URL로 요청이 들어오면 권한을 확인
         *  .addFilterBefore()는 필터를 등록한다. 파라미터는 2개 -> 커스텀한 필터링과 오른쪽에 등록한 필터 전에 커스텀필터링이 수행*/

        http.csrf().disable();  // csrf 설정을 비활성화
        //http.httpBasic().disable(); // 일반적인 루트가 아닌 다른 방식으로 요청시 거절, header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다.
        http.httpBasic().disable()
                .authorizeRequests()// 요청에 대한 사용권한 체크

                //회원관련 api는 비밀번호 재설정 빼고는 토큰 필요 x
//                .antMatchers("/users/password-restore").hasRole("USER")
                .antMatchers("/users/**").permitAll()   // 누구나 접근이 가능하게 설정
                // /admin으로 시작하는 요청은 ADMIN 권한이 있는 유저에게만 허용
                .antMatchers("/admin/**").hasRole("ADMIN")
                //그외 웹으로 들어오는 요청 모두 권한 확인
                .antMatchers("/**").hasRole("USER")
                //말고 그냥 들어오는 모든 요청 거절
                .anyRequest().denyAll()
                .and()

                // + 토큰에 저장된 유저정보를 활용하여야 하기 때문에 CustomUserDetailService 클래스를 생성합니다.
                // 토큰을 사용하기 때문에 세션은 필요 없다.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
