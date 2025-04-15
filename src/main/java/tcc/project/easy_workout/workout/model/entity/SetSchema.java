package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tb_set_schemas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "reps")
    private Integer reps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_schema_id")
    private WorkoutSchema workoutSchema;
}
