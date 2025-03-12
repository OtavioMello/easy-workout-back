package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tb_sets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Set {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "weight")
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private WorkoutInstance workoutInstance;
}
