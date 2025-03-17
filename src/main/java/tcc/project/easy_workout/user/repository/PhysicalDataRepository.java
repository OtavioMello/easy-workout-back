package tcc.project.easy_workout.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcc.project.easy_workout.user.model.entity.PhysicalData;

import java.time.LocalDate;
import java.util.Optional;

public interface PhysicalDataRepository extends JpaRepository<PhysicalData, String> {

    Optional<PhysicalData> findByCreatedAtAndTraineeId(LocalDate createdAt, String traineeId);
}
