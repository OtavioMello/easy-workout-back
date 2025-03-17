package tcc.project.easy_workout.user.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcc.project.easy_workout.user.model.dto.TraineeRequestDto;
import tcc.project.easy_workout.user.model.dto.TraineeResponseDto;
import tcc.project.easy_workout.user.service.TraineeService;

import java.net.URI;

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeController.class);

    @PostMapping
    public ResponseEntity<URI> createTrainee(@RequestBody TraineeRequestDto request) {
        LOGGER.info("[TraineeController] Calling createTrainee");
        return ResponseEntity.ok(traineeService.createTrainee(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TraineeResponseDto> getTraineeById(@PathVariable String id, @RequestHeader("Authorization") String authorization) {
        LOGGER.info("[TraineeController] Calling getTraineeById: id={}", id);
        return ResponseEntity.ok(traineeService.getTraineeById(id, authorization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TraineeResponseDto> updateTrainee(@PathVariable String id, @RequestHeader("Authorization") String authorization, @RequestBody TraineeRequestDto request) {
        LOGGER.info("[TraineeController] Calling updateTrainee: id={}", id);
        return ResponseEntity.ok(traineeService.updateTrainee(id, authorization, request));
    }

}
