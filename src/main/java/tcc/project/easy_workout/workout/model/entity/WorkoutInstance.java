package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_workout_instances")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workout_schema_id")
    private WorkoutSchema schema;

    @OneToMany(mappedBy = "workoutInstance", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Set> sets = new ArrayList<>();

    @Column(name = "completed")
    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_routine_instance_id")
    private WorkoutRoutineInstance workoutRoutineInstance;

}
