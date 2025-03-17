package tcc.project.easy_workout.auth.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tcc.project.easy_workout.common.exception.model.CustomExceptionResponse;
import tcc.project.easy_workout.user.model.entity.User;
import tcc.project.easy_workout.user.repository.PersonalTrainerRepository;
import tcc.project.easy_workout.user.repository.TraineeRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final JsonWebTokenService jsonWebTokenService;
    private final PersonalTrainerRepository personalTrainerRepository;
    private final TraineeRepository traineeRepository;
    private final ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recoverToken(request);

        if (token == null){
            filterChain.doFilter(request, response);
            return;
        }

        if(Boolean.FALSE.equals(jsonWebTokenService.isValidToken(token))){
            returnExpiredTokenException(request, response, token);
            return;
        }

        var userId = JsonWebTokenService.getUserId(token);
        var role = getRoleFromToken(token);

        User user = switch (role){
            case "personal_trainer" -> personalTrainerRepository.findById(userId)
                    .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));
            case "trainee" -> traineeRepository.findById(userId)
                    .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));
            default -> throw new AuthenticationCredentialsNotFoundException("Invalid user role");
        };

        if ("personal_trainer".equals(role) && request.getServletPath().startsWith("/trainees")){
            validateTraineeAccess(user.getId(), request);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void returnExpiredTokenException(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        var customException = CustomExceptionResponse.builder()
                .message("Token expired in " + JWT.decode(token).getExpiresAt().toInstant())
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(customException));
        response.flushBuffer();
    }

    private String getRoleFromToken(String token) {
        return JWT.decode(token).getClaim("role").asString();
    }

    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);
    }

    private void validateTraineeAccess(String personalTrainerId, HttpServletRequest request){
        if (request.getServletPath().matches("/trainee/\\d+")){
            var traineeId = request.getServletPath().split("/")[2];
            var trainee = traineeRepository.findById(traineeId);

            if (trainee.isEmpty() || !trainee.get().getPersonalTrainer().getId().equals(personalTrainerId)){
                throw new AccessDeniedException("Authenticated user can't perform this action");
            }
        }
    }
}
