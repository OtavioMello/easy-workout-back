package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;
import tcc.project.easy_workout.user.model.entity.Trainee;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_workout_routine_executions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutRoutineExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "workout_routine_execution_workouts",
            joinColumns = @JoinColumn(name = "workout_routine_execution_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id"))
    private List<WorkoutInstance> workoutInstances = new ArrayList<>();

    @Column(name = "completed")
    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

}
