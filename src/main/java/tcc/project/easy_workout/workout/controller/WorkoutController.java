package tcc.project.easy_workout.workout.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcc.project.easy_workout.workout.model.dto.request.WorkoutInstanceUpdateRequestDto;
import tcc.project.easy_workout.workout.model.dto.request.WorkoutRoutineSchemaRequestDto;
import tcc.project.easy_workout.workout.model.dto.response.WorkoutRoutineInstanceResponseDto;
import tcc.project.easy_workout.workout.model.dto.response.WorkoutRoutineSchemaResponseDto;
import tcc.project.easy_workout.workout.model.dto.response.WorkoutTemplateResponseDto;
import tcc.project.easy_workout.workout.service.WorkoutService;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkoutController.class);
    private final WorkoutService workoutService;

    /* WORKOUTS SCHEMAS */

    @GetMapping("/templates")
    public ResponseEntity<List<WorkoutTemplateResponseDto>> getAllWorkoutTemplates(@RequestHeader("Authorization") String authorization) {
        LOGGER.info("[WorkoutController] Calling getAllWorkoutTemplates");
        return ResponseEntity.ok(workoutService.getAllWorkoutTemplates());
    }

    @GetMapping("/templates/{workoutTemplateId}")
    public ResponseEntity<WorkoutTemplateResponseDto> getWorkoutSchemaById(@PathVariable String workoutTemplateId, @RequestHeader("Authorization") String authorization) {
        LOGGER.info("[WorkoutController] Calling getWorkoutTemplateById: workoutTemplateId={}", workoutTemplateId);
        return ResponseEntity.ok(workoutService.getWorkoutTemplateById(workoutTemplateId));
    }

    /* WORKOUTS INSTANCES */

    @PutMapping("/instances/{workoutInstanceId}")
    public ResponseEntity<Void> updateWorkoutInstanceById(@RequestBody WorkoutInstanceUpdateRequestDto request, @PathVariable String workoutInstanceId, @RequestHeader("Authorization") String authorization) {
        LOGGER.info("[WorkoutController] Calling updateWorkoutInstanceById: workoutInstanceId={}", workoutInstanceId);
        return ResponseEntity.ok(workoutService.updateWorkoutInstanceById(request, workoutInstanceId, authorization));
    }

    /* WORKOUT ROUTINES SCHEMAS */

    @PostMapping("/routines/schemas/trainees/{traineeId}")
    public ResponseEntity<Void> addWorkoutRoutineSchemasToTrainee(@RequestBody List<WorkoutRoutineSchemaRequestDto> request, @PathVariable String traineeId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling addWorkoutsToTrainee: traineeId={}", traineeId);
        return ResponseEntity.ok(workoutService.addWorkoutRoutineSchemasToTrainee(request, traineeId, authorization));
    }

    @GetMapping("/routines/schemas/trainees/{traineeId}")
    public ResponseEntity<List<WorkoutRoutineSchemaResponseDto>> getAllTraineeWorkoutRoutineSchemas(@PathVariable String traineeId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling getAllTraineeWorkoutsInstances: traineeId={}", traineeId);
        return ResponseEntity.ok(workoutService.getAllTraineeWorkoutRoutineSchemas(traineeId, authorization));
    }

    @GetMapping("/routines/schemas/{workoutRoutineSchemaId}")
    public ResponseEntity<WorkoutRoutineSchemaResponseDto> getWorkoutRoutineSchemaById(@PathVariable String workoutRoutineSchemaId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling getWorkoutRoutineById: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        return ResponseEntity.ok(workoutService.getWorkoutRoutineSchemaById(workoutRoutineSchemaId, authorization));
    }

    @GetMapping("/routines/schemas/{workoutRoutineSchemaId}/instance")
    public ResponseEntity<WorkoutRoutineInstanceResponseDto> getWorkoutRoutineInstanceBySchemaId(@PathVariable String workoutRoutineSchemaId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling getWorkoutRoutineInstanceBySchemaId: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        return ResponseEntity.ok(workoutService.getWorkoutRoutineInstanceBySchemaId(workoutRoutineSchemaId, authorization));
    }

    @PutMapping("/routines/schemas/{workoutRoutineSchemaId}")
    public ResponseEntity<WorkoutRoutineSchemaResponseDto> updateWorkoutRoutineSchemaById(@RequestBody WorkoutRoutineSchemaRequestDto request, @PathVariable String workoutRoutineSchemaId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling updateWorkoutRoutineById: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        return ResponseEntity.ok(workoutService.updateWorkoutRoutineSchemaById(request, workoutRoutineSchemaId, authorization));
    }

    @DeleteMapping("/routines/schemas/{workoutRoutineSchemaId}")
    public ResponseEntity<Void> deleteWorkoutRoutineSchemaById(@PathVariable String workoutRoutineSchemaId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling deleteWorkoutRoutineById: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        return ResponseEntity.ok(workoutService.deleteWorkoutRoutineSchemaById(workoutRoutineSchemaId, authorization));
    }

    /* WORKOUT ROUTINES INSTANCES */

    @GetMapping("/routines/instances/trainees/{traineeId}")
    public ResponseEntity<List<WorkoutRoutineInstanceResponseDto>> getAllTraineeWorkoutRoutineInstances(@PathVariable String traineeId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling getAllTraineeWorkoutRoutineInstances: traineeId={}", traineeId);
        return ResponseEntity.ok(workoutService.getAllTraineeWorkoutRoutineInstances(traineeId, authorization));
    }

    @GetMapping("/routines/instances/{workoutRoutineInstanceId}")
    public ResponseEntity<WorkoutRoutineInstanceResponseDto> getWorkoutRoutineInstanceById(@PathVariable String workoutRoutineInstanceId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling getWorkoutRoutineInstanceById: workoutRoutineInstanceId={}", workoutRoutineInstanceId);
        return ResponseEntity.ok(workoutService.getWorkoutRoutineInstanceById(workoutRoutineInstanceId, authorization));
    }

    @PutMapping("/routines/instances/{workoutRoutineInstanceId}/complete")
    public ResponseEntity<Void> updateWorkoutRoutineInstanceById(@PathVariable String workoutRoutineInstanceId, @RequestHeader("Authorization") String authorization){
        LOGGER.info("[WorkoutController] Calling updateWorkoutRoutineInstanceById: workoutRoutineInstanceId={}", workoutRoutineInstanceId);
        return ResponseEntity.ok(workoutService.updateWorkoutRoutineInstanceById(workoutRoutineInstanceId, authorization));
    }

}
