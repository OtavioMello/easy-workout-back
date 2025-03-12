package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;
import tcc.project.easy_workout.user.model.entity.Trainee;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_workout_routine_instances")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutRoutineInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_routine_schema_id")
    private WorkoutRoutineSchema schema;

    @OneToMany(mappedBy = "workoutRoutineInstance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WorkoutInstance> workoutInstances = new ArrayList<>();

    @Column(name = "completed")
    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

}
