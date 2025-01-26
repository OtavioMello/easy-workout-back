package tcc.project.easy_workout.auth.security;

import org.springframework.security.core.Authentication;

public interface JsonWebTokenService {

    String createToken(Authentication authentication);
    String getUserId(String token);
    Boolean isValidToken(String token);
}
