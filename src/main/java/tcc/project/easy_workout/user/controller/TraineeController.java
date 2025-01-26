package tcc.project.easy_workout.user.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcc.project.easy_workout.user.model.dto.TraineeDto;
import tcc.project.easy_workout.user.service.TraineeService;

import java.net.URI;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeController.class);

    @PostMapping
    public ResponseEntity<URI> createTrainee(@RequestBody TraineeDto request) {
        LOGGER.info("[TraineeController] Calling createTrainee");
        return ResponseEntity.ok(traineeService.createTrainee(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TraineeDto> getTraineeById(@PathVariable String id, @RequestHeader("Authorization") String authorization) {
        LOGGER.info("[TraineeController] Calling getTraineeById: id={}", id);
        return ResponseEntity.ok(traineeService.getTraineeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TraineeDto> updateTrainee(@PathVariable String id, @RequestHeader("Authorization") String authorization, @RequestBody TraineeDto request) {
        LOGGER.info("[TraineeController] Calling updateTrainee: id={}", id);
        return ResponseEntity.ok(traineeService.updateTrainee(id, request));
    }

}
