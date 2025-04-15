package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_workout_schemas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_template_id", referencedColumnName = "id")
    private WorkoutTemplate template;

    @OneToMany(mappedBy = "workoutSchema", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SetSchema> sets = new ArrayList<>();
}
