package tcc.project.easy_workout.auth.security;

import com.auth0.jwt.JWT;
import org.springframework.security.core.Authentication;

public interface JsonWebTokenService {

    String createToken(Authentication authentication);

    static String getUserId(String token) {
        return JWT.decode(token).getSubject();
    }

    Boolean isValidToken(String token);
}
