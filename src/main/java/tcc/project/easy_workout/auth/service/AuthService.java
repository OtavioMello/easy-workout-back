package tcc.project.easy_workout.auth.service;

import tcc.project.easy_workout.auth.model.dto.AuthRequestDto;
import tcc.project.easy_workout.auth.model.dto.AuthResponseDto;

public interface AuthService {

    AuthResponseDto authenticate(AuthRequestDto request);
}
