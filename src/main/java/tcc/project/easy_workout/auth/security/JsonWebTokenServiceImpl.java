package tcc.project.easy_workout.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tcc.project.easy_workout.user.model.entity.PersonalTrainer;
import tcc.project.easy_workout.user.model.entity.Trainee;
import tcc.project.easy_workout.user.model.entity.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JsonWebTokenServiceImpl implements JsonWebTokenService{

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.issuer}")
    private String issuer;

    @Override
    public String createToken(Authentication authentication) {

        Algorithm algorithm = Algorithm.HMAC256(secret);
        var user = (User) authentication.getPrincipal();
        var userRole = getUserRole(user);

        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(getIssuedAt())
                .withExpiresAt(getExpiresAt())
                .withSubject(user.getId())
                .withClaim("role", userRole)
                .sign(algorithm);

    }

    @Override
    public String getUserId(String token) {
        return JWT.decode(token).getSubject();
    }

    @Override
    public Boolean isValidToken(String token) {
        return JWT.decode(token).getExpiresAt().after(new Date(System.currentTimeMillis()));
    }

    private Instant getIssuedAt() {
        return new Date(System.currentTimeMillis()).toInstant();
    }

    private Instant getExpiresAt() {
        return new Date(System.currentTimeMillis()).toInstant().plus(30, ChronoUnit.MINUTES);
    }

    private String getUserRole(User user) {
        if (user instanceof PersonalTrainer){
            return "personal_trainer";
        }

        if (user instanceof Trainee){
            return "trainee";
        }

        return null;
    }

}
