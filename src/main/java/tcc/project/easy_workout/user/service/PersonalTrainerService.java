package tcc.project.easy_workout.user.service;

import tcc.project.easy_workout.user.model.dto.PersonalTrainerDto;

import java.net.URI;

public interface PersonalTrainerService {

    URI createPersonalTrainer(PersonalTrainerDto request);
    PersonalTrainerDto getPersonalTrainerById(String id);
    PersonalTrainerDto updatePersonalTrainer(String id, PersonalTrainerDto request);
}
