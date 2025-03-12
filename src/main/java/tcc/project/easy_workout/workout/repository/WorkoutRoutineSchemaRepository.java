package tcc.project.easy_workout.workout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcc.project.easy_workout.workout.model.entity.WorkoutRoutineSchema;

import java.util.List;

public interface WorkoutRoutineSchemaRepository extends JpaRepository<WorkoutRoutineSchema, String> {

    List<WorkoutRoutineSchema> findAllByTraineeId(String traineeId);
}
