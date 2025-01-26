package tcc.project.easy_workout.auth.service;

import tcc.project.easy_workout.auth.model.dto.AuthDto;
import tcc.project.easy_workout.auth.model.dto.TokenDto;

public interface AuthService {

    TokenDto authenticate(AuthDto request);
}
