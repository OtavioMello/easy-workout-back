package tcc.project.easy_workout.workout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcc.project.easy_workout.workout.model.entity.WorkoutTemplate;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, String> {
}
