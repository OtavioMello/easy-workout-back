package tcc.project.easy_workout.workout.service;

import tcc.project.easy_workout.workout.model.dto.request.WorkoutInstanceUpdateRequestDto;
import tcc.project.easy_workout.workout.model.dto.request.WorkoutRoutineInstanceRequestDto;
import tcc.project.easy_workout.workout.model.dto.request.WorkoutRoutineSchemaRequestDto;
import tcc.project.easy_workout.workout.model.dto.response.WorkoutRoutineInstanceResponseDto;
import tcc.project.easy_workout.workout.model.dto.response.WorkoutRoutineSchemaResponseDto;
import tcc.project.easy_workout.workout.model.dto.response.WorkoutSchemaResponseDto;

import java.util.List;

public interface WorkoutService {

    WorkoutSchemaResponseDto getWorkoutSchemaById(String workoutSchemaId);
    List<WorkoutSchemaResponseDto> getAllWorkoutSchemas();
    Void updateWorkoutInstanceById(WorkoutInstanceUpdateRequestDto request, String workoutInstanceId, String authorization);
    Void addWorkoutRoutineSchemasToTrainee(List<WorkoutRoutineSchemaRequestDto> request, String traineeId, String authorization);
    WorkoutRoutineSchemaResponseDto getWorkoutRoutineSchemaById(String workoutRoutineSchemaId, String authorization);
    List<WorkoutRoutineSchemaResponseDto> getAllTraineeWorkoutRoutineSchemas(String traineeId, String authorization);
    WorkoutRoutineSchemaResponseDto updateWorkoutRoutineSchemaById(WorkoutRoutineSchemaRequestDto request, String workoutRoutineSchemaId, String authorization);
    Void deleteWorkoutRoutineSchemaById(String workoutRoutineSchemaId, String authorization);
    Void addWorkoutRoutineInstancesToTrainee(List<WorkoutRoutineInstanceRequestDto> request, String traineeId, String authorization);
    List<WorkoutRoutineInstanceResponseDto> getAllTraineeWorkoutRoutineInstances(String traineeId, String authorization);
    WorkoutRoutineInstanceResponseDto getWorkoutRoutineInstanceById(String workoutRoutineInstanceId, String authorization);
    Void updateWorkoutRoutineInstanceById(String workoutRoutineInstanceId, String authorization);
}
