package tcc.project.easy_workout.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcc.project.easy_workout.user.model.entity.Trainee;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, String> {

    Optional<Trainee> findByEmail(String email);
}
