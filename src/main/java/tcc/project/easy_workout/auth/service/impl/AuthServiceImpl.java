package tcc.project.easy_workout.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tcc.project.easy_workout.auth.model.dto.AuthDto;
import tcc.project.easy_workout.auth.model.dto.TokenDto;
import tcc.project.easy_workout.auth.security.JsonWebTokenService;
import tcc.project.easy_workout.auth.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JsonWebTokenService jsonWebTokenService;

    @Override
    public TokenDto authenticate(AuthDto request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var token = jsonWebTokenService.createToken(authentication);
        LOGGER.info("[AuthServiceImpl] User successfully authenticated");
        return new TokenDto(token);
    }
}
