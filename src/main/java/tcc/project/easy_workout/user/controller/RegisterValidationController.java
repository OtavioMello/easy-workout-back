package tcc.project.easy_workout.user.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcc.project.easy_workout.user.model.dto.RegisterValidationDto;
import tcc.project.easy_workout.user.service.RegisterValidationService;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterValidationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterValidationController.class);
    private final RegisterValidationService registerValidationService;

    @GetMapping("/validate/email")
    public ResponseEntity<RegisterValidationDto> validateEmail(@RequestParam String email){
        LOGGER.info("[RegisterValidationController] Calling validateEmail");
        return ResponseEntity.ok(registerValidationService.validateEmail(email));
    }

    @GetMapping("/validate/nickname")
    public ResponseEntity<RegisterValidationDto> validateEmailNickname(@RequestParam String nickname){
        LOGGER.info("[RegisterValidationController] Calling validateNickname");
        return ResponseEntity.ok(registerValidationService.validateNickname(nickname));
    }
}
