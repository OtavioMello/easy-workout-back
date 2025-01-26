package tcc.project.easy_workout.auth.controller;

import lombok.RequiredArgsConstructor;
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

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<TokenDto> authenticate(@RequestBody AuthDto request){
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
