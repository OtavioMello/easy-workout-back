package tcc.project.easy_workout.common.exception.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tcc.project.easy_workout.common.exception.model.ConflictException;
import tcc.project.easy_workout.common.exception.model.CustomExceptionResponse;

import javax.ws.rs.*;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;

@RestControllerAdvice
@RequiredArgsConstructor
public class HandlerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerController.class);
    private static final String LOG_TEMPLATE = "[Exception] An error occurred: status={}, message={}";
    private final ObjectMapper objectMapper;

    public void accessDeniedHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        LOGGER.error(LOG_TEMPLATE, HttpStatus.FORBIDDEN, accessDeniedException.getMessage());
        var customExceptionMessage = CustomExceptionResponse.builder()
                .message(accessDeniedException.getMessage())
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(customExceptionMessage));
    }

    public void authenticationHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        LOGGER.error(LOG_TEMPLATE, HttpStatus.FORBIDDEN, authenticationException.getMessage());
        var customExceptionMessage = CustomExceptionResponse.builder()
                .message(authenticationException.getMessage())
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(customExceptionMessage));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<CustomExceptionResponse> notFoundExceptionHandler(NotFoundException exception, HttpServletRequest request) {
        var customExceptionMessage = getCustomExceptionMessage(exception, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<CustomExceptionResponse> usernameNotFoundExceptionHandler(UsernameNotFoundException exception, HttpServletRequest request) {
        LOGGER.error(LOG_TEMPLATE, HttpStatus.NOT_FOUND, exception.getMessage());
        var customExceptionMessage = CustomExceptionResponse.builder()
                .message(exception.getMessage())
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<CustomExceptionResponse> badRequestExceptionHandler(BadRequestException exception, HttpServletRequest request) {
        var customExceptionMessage = getCustomExceptionMessage(exception, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomExceptionResponse> conflictExceptionHandler(ConflictException exception, HttpServletRequest request){
        var customExceptionMessage = getCustomExceptionMessage(exception, request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CustomExceptionResponse> sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException exception, HttpServletRequest request) {
        var customExceptionMessage = CustomExceptionResponse.builder()
                .message("Constraint violation")
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<CustomExceptionResponse> forbiddenExceptionHandler(ForbiddenException exception, HttpServletRequest request) {
        var customExceptionMessage = getCustomExceptionMessage(exception, request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({NotAuthorizedException.class})
    public ResponseEntity<CustomExceptionResponse> notAuthorizedExceptionHandler(NotAuthorizedException exception, HttpServletRequest request) {
        var customExceptionMessage = getCustomExceptionMessage(exception, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({TokenExpiredException.class})
    public ResponseEntity<CustomExceptionResponse> tokenExpiredExceptionHandler(TokenExpiredException exception, HttpServletRequest request) {
        var customExceptionMessage = CustomExceptionResponse.builder()
                .message(exception.getMessage())
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({CredentialsExpiredException.class})
    public ResponseEntity<CustomExceptionResponse> credentialsExpiredExceptionHandler(CredentialsExpiredException exception, HttpServletRequest request) {
        var customExceptionMessage = CustomExceptionResponse.builder()
                .message(exception.getMessage())
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(customExceptionMessage);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<CustomExceptionResponse> authenticationExceptionHandler(AuthenticationException exception, HttpServletRequest request) {
        var customExceptionMessage = CustomExceptionResponse.builder()
                .message(exception.getMessage())
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(customExceptionMessage);
    }

    private CustomExceptionResponse getCustomExceptionMessage(WebApplicationException exception, HttpServletRequest request) {
        LOGGER.error(LOG_TEMPLATE, exception.getResponse().getStatus(), exception.getMessage());
        return CustomExceptionResponse.builder()
                .message(exception.getMessage())
                .details(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
    }
}
