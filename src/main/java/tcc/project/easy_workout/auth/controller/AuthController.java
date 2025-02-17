package tcc.project.easy_workout.auth.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcc.project.easy_workout.auth.model.dto.AuthDto;
import tcc.project.easy_workout.auth.model.dto.TokenDto;
import tcc.project.easy_workout.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<TokenDto> authenticate(@RequestBody AuthDto request){
        LOGGER.info("[AuthController] Calling authenticate");
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
