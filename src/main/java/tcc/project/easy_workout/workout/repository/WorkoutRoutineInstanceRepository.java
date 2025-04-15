package tcc.project.easy_workout.workout.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcc.project.easy_workout.workout.model.entity.WorkoutRoutineInstance;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRoutineInstanceRepository extends JpaRepository<WorkoutRoutineInstance, String> {

    @EntityGraph(attributePaths = {"schema", "workoutInstances.schema.template.equipment"})
    List<WorkoutRoutineInstance> findAllByTraineeId(String traineeId);

    Optional<WorkoutRoutineInstance> findBySchemaId(String schemaId);
}
