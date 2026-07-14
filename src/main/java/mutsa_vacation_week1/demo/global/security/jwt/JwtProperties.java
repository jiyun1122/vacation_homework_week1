package mutsa_vacation_week1.demo.global.security.jwt;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String secret;
    private final long accessTokenExpiration;

    public JwtProperties(String secret, long accessTokenExpiration) {
        this.secret = secret;
        this.accessTokenExpiration = accessTokenExpiration;
    }
}
