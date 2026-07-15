package mutsa_vacation_week1.demo.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String CLAIM_LOGIN_ID = "loginId";

    private final SecretKey key;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${JWT_SECRET}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String createAccessToken(Long memberId, String loginId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim(CLAIM_LOGIN_ID, loginId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getMemberId(Claims claims) {
        return Long.parseLong(claims.getSubject());
    }

    public String getLoginId(Claims claims) {
        return claims.get(CLAIM_LOGIN_ID, String.class);
    }

}
