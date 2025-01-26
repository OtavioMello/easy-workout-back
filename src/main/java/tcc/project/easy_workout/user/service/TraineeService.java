package tcc.project.easy_workout.user.service;

import org.springframework.http.ResponseEntity;
import tcc.project.easy_workout.user.model.dto.TraineeDto;

import java.net.URI;

public interface TraineeService {

    URI createTrainee(TraineeDto request);

    TraineeDto getTraineeById(String id);

    TraineeDto updateTrainee(String id, TraineeDto request);
}
