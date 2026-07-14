package mutsa_vacation_week1.demo.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.global.apiPayload.code.BaseErrorCode;
import org.springframework.http.MediaType;

import java.io.IOException;


public final class JwtAuthExceptionResponseWriter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JwtAuthExceptionResponseWriter() {
    }

    public static void write(HttpServletResponse response, BaseErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                OBJECT_MAPPER.writeValueAsString(ApiResponse.onFailure(errorCode.getMessage()))
        );
    }

}
