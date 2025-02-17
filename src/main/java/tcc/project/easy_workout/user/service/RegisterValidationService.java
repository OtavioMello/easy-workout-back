package tcc.project.easy_workout.user.service;

import tcc.project.easy_workout.user.model.dto.RegisterValidationDto;

public interface RegisterValidationService {

    RegisterValidationDto validateEmail(String email);
    RegisterValidationDto validateNickname(String nickname);
}
