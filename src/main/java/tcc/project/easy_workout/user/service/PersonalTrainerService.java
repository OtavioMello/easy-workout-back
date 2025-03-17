package tcc.project.easy_workout.user.service;

import tcc.project.easy_workout.user.model.dto.PersonalTrainerRequestDto;
import tcc.project.easy_workout.user.model.dto.PersonalTrainerResponseDto;

import java.net.URI;

public interface PersonalTrainerService {

    URI createPersonalTrainer(PersonalTrainerRequestDto request);
    Void addTraineeToPersonalTrainer(String personalTrainerId, String traineeId, String authorization);
    PersonalTrainerResponseDto getPersonalTrainerById(String id, String authorization);
    PersonalTrainerResponseDto updatePersonalTrainer(String id, String authorization, PersonalTrainerRequestDto request);
}
