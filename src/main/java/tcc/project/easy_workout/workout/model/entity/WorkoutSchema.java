package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tb_workout_schemas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

}
