package tcc.project.easy_workout.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcc.project.easy_workout.user.model.entity.PersonalTrainer;

import java.util.Optional;

@Repository
public interface PersonalTrainerRepository extends JpaRepository<PersonalTrainer, String> {

    Optional<PersonalTrainer> findByEmail(String email);
}
