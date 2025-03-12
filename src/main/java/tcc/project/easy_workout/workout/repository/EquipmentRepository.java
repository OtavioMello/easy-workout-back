package tcc.project.easy_workout.workout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcc.project.easy_workout.workout.model.entity.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {
}
