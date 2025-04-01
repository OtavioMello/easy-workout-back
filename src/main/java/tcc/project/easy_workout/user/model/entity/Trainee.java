package tcc.project.easy_workout.user.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import tcc.project.easy_workout.workout.model.entity.WorkoutRoutineInstance;

import java.time.LocalDate;
import java.util.*;

@Entity(name = "tb_trainee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Trainee extends User {

    @Column(name = "birthdate")
    private LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @OneToMany(mappedBy = "trainee", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WorkoutRoutineInstance> workoutRoutineInstances = new ArrayList<>();

    @OneToMany(mappedBy = "trainee", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PhysicalData> physicalData = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_trainer_id")
    PersonalTrainer personalTrainer;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "trainee_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

}
