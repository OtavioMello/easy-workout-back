package tcc.project.easy_workout.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcc.project.easy_workout.user.model.entity.Role;
import tcc.project.easy_workout.user.model.entity.enums.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(RoleName name);
}
