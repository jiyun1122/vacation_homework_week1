package mutsa_vacation_week1.demo.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.code.AuthErrorCode;
import mutsa_vacation_week1.demo.global.security.AuthMember;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            JwtAuthExceptionResponseWriter.write(response, AuthErrorCode.INVALID_TOKEN_FORMAT);
            return;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            Claims claims = jwtTokenProvider.parseClaims(token);
            AuthMember authMember = new AuthMember(
                    jwtTokenProvider.getMemberId(claims),
                    jwtTokenProvider.getLoginId(claims)
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(authMember, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException e) {
            JwtAuthExceptionResponseWriter.write(response, AuthErrorCode.EXPIRED_TOKEN);
            return;
        } catch (JwtException | IllegalArgumentException e) {
            JwtAuthExceptionResponseWriter.write(response, AuthErrorCode.INVALID_TOKEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
