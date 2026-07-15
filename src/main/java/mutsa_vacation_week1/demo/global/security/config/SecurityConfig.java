package mutsa_vacation_week1.demo.global.security.config;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.security.handler.JwtAccessDeniedHandler;
import mutsa_vacation_week1.demo.global.security.handler.JwtAuthenticationEntryPoint;
import mutsa_vacation_week1.demo.global.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
모든 요청은 JWT 토큰 검사 필터를 거치게 하고, 특정 회원가입/로그인 URL을 제외한 모든 API는 로그인 인증을 통해 접근할 수 있도록 함
JwtAuthenticationFilter:jwt 토큰이 유효한지 검사
jwtAuthenticationEntryPoint: 인증되지 않은 사용자가 보호된 리소스에 접근할 때 401 오류 처리를 하도록 함
wtAccessDeniedHandler: 인증되었지만 권한이 없는 사용자의 경우 403 오류 처리를 하도록 함

 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/members").permitAll()
                        .requestMatchers(HttpMethod.POST, "/members/auth/login").permitAll()
                        .requestMatchers("/api/credit/**").authenticated()
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/cart/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
