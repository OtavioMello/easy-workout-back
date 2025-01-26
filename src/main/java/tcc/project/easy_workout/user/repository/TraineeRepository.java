package tcc.project.easy_workout.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcc.project.easy_workout.user.model.entity.Trainee;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, String> {

    Trainee findByEmail(String email);
}
