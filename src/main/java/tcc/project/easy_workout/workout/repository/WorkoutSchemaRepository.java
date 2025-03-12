package tcc.project.easy_workout.workout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcc.project.easy_workout.workout.model.entity.WorkoutSchema;

import java.util.List;

@Repository
public interface WorkoutSchemaRepository extends JpaRepository<WorkoutSchema, String> {

    List<WorkoutSchema> findByIdIn(List<String> workouts);
}
