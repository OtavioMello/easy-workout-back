package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "tb_workouts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "workout", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Set> sets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    @Column(name = "completed")
    private Boolean completed;

}
