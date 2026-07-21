package mutsa_vacation_week1.demo.global.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // 1. 인증 컴포넌트 이름 정의
        String securityJwtName = "JWT_Authentication";

        // 2. 모든 API 요청 시 이 인증을 기본 요구하도록 설정 (자물쇠 아이콘 표시 목적)
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securityJwtName);

        // 3. 어떤 방식으로 토큰을 넣을지 스웨거에 상세 정의 (Bearer JWT 방식)
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securityJwtName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("로그인 후 발급받은 Access Token을 입력해 주세요. (Bearer 단어 제외하고 토큰만 입력)");

        return new OpenAPI()
                .info(new Info()
                        .title("Shopping Mall API 문서")
                        .description("장바구니 및 주문 API 명세서")
                        .version("v1.0"))
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes(securityJwtName, securityScheme));
    }
}