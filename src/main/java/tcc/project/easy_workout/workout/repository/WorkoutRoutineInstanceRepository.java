package tcc.project.easy_workout.workout.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcc.project.easy_workout.workout.model.entity.WorkoutRoutineInstance;

import java.util.List;

@Repository
public interface WorkoutRoutineInstanceRepository extends JpaRepository<WorkoutRoutineInstance, String> {

    @EntityGraph(attributePaths = {"schema", "workoutInstances.schema.equipment"})
    List<WorkoutRoutineInstance> findAllByTraineeId(String traineeId);
}
