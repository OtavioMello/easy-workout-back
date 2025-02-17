package tcc.project.easy_workout.auth.security;

import com.auth0.jwt.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.slf4j.Logger;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tcc.project.easy_workout.user.model.entity.User;
import tcc.project.easy_workout.user.repository.PersonalTrainerRepository;
import tcc.project.easy_workout.user.repository.TraineeRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JsonWebTokenService jsonWebTokenService;
    private final PersonalTrainerRepository personalTrainerRepository;
    private final TraineeRepository traineeRepository;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(isPrivateRoute(request)) {

            var token = recoverToken(request);

            if(Boolean.FALSE.equals(jsonWebTokenService.isValidToken(token))){
                throw new CredentialsExpiredException(new StringFormattedMessage("Expired token {0}", JWT.decode(token).getExpiresAt().toInstant()).toString());
            }

            var userId = jsonWebTokenService.getUserId(token);
            validateRequestUserId(request, userId);
            var role = getRoleFromToken(token);

            User user = null;

            if(role.equals("personal_trainer")){
                user = personalTrainerRepository.findById(userId).orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));
            }

            if (role.equals("trainee")){
                user = traineeRepository.findById(userId).orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private void validateRequestUserId(HttpServletRequest request, String userId) {
        if(!request.getRequestURI().contains(userId)){
            throw new InsufficientAuthenticationException("Authenticated user hasn't access to this resource");
        }
    }

    private String getRoleFromToken(String token) {
        return JWT.decode(token).getClaim("role").asString();
    }

    private boolean isPrivateRoute(HttpServletRequest request) {
        var routes = List.of( "/trainee", "/personal-trainer");
        var path = request.getRequestURI();
        return routes.stream().anyMatch(path::contains) && !request.getMethod().equals("POST");
    }

    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(Objects.nonNull(authorizationHeader)) {
            return authorizationHeader.replace("Bearer ", "");
        }
        throw new BadCredentialsException("Authorization token not found");
    }
}
