package mutsa_vacation_week1.demo.global.security.config;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.security.handler.JwtAccessDeniedHandler;
import mutsa_vacation_week1.demo.global.security.handler.JwtAuthenticationEntryPoint;
import mutsa_vacation_week1.demo.global.security.jwt.JwtAuthenticationFilter;
import mutsa_vacation_week1.demo.global.security.oauth2.CustomOAuth2UserService;
import mutsa_vacation_week1.demo.global.security.oauth2.OAuth2LoginFailureHandler;
import mutsa_vacation_week1.demo.global.security.oauth2.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // cors 설정 추가, 이거 누락되어있었음
                .csrf(AbstractHttpConfigurer::disable)
                // 카카오 OAuth2 인가 요청(state) 저장을 위해 STATELESS -> IF_REQUIRED로 변경
                // 일반 Bearer API 요청은 세션을 만들지 않으므로 실질적으로는 그대로 stateless 하게 동작함
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 반드시 최상단 근처에 추가, 이 부분이 누락되었었음
                        .requestMatchers(HttpMethod.POST, "/members").permitAll()
                        .requestMatchers(HttpMethod.POST, "/members/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/menu", "/menu/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/stores", "/stores/**").permitAll()
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/credit/**").authenticated()
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/cart/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() { // 누락된 cors 설정 관련 추가
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "https://likelion-week7.vercel.app",
                "https://mutsa-vacation-team1.site",
                "https://www.mutsa-vacation-team1.site"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

/* 이건 커밋을 위한 커밋이다
 */