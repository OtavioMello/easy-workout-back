package tcc.project.easy_workout.user.service;

import tcc.project.easy_workout.user.model.dto.TraineeDto;

import java.net.URI;

public interface TraineeService {

    URI createTrainee(TraineeDto request);

    TraineeDto getTraineeById(String id, String authorization);

    TraineeDto updateTrainee(String id, String authorization, TraineeDto request);
}
