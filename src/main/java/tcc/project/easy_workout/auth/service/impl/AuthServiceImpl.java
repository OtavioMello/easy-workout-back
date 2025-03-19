package tcc.project.easy_workout.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tcc.project.easy_workout.auth.model.dto.AuthRequestDto;
import tcc.project.easy_workout.auth.model.dto.AuthResponseDto;
import tcc.project.easy_workout.auth.security.JsonWebTokenService;
import tcc.project.easy_workout.auth.service.AuthService;
import tcc.project.easy_workout.user.repository.PersonalTrainerRepository;
import tcc.project.easy_workout.user.repository.TraineeRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JsonWebTokenService jsonWebTokenService;
    private final PersonalTrainerRepository personalTrainerRepository;
    private final TraineeRepository traineeRepository;

    @Override
    public AuthResponseDto authenticate(AuthRequestDto request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var token = jsonWebTokenService.createToken(authentication);

        var personalTrainer = personalTrainerRepository.findByEmail(request.getEmail());
        if (personalTrainer.isPresent()){
            LOGGER.info("[AuthServiceImpl] User successfully authenticated");
            return new AuthResponseDto(token, personalTrainer.get().getId(), "PERSONAL_TRAINER");
        }

        var trainee = traineeRepository.findByEmail(request.getEmail());
        if (trainee.isPresent()){
            LOGGER.info("[AuthServiceImpl] User successfully authenticated");
            return new AuthResponseDto(token, trainee.get().getId(), "TRAINEE");
        }

        LOGGER.error("[AuthServiceImpl] An error occurred during authentication: User not found");
        throw new AuthenticationCredentialsNotFoundException("User not found");
    }
}
