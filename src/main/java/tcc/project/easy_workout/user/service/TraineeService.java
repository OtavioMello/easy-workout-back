package tcc.project.easy_workout.user.service;

import tcc.project.easy_workout.user.model.dto.TraineeRequestDto;
import tcc.project.easy_workout.user.model.dto.TraineeResponseDto;

import java.net.URI;

public interface TraineeService {

    URI createTrainee(TraineeRequestDto request);

    TraineeResponseDto getTraineeById(String id, String authorization);

    TraineeResponseDto updateTrainee(String id, String authorization, TraineeRequestDto request);
}
