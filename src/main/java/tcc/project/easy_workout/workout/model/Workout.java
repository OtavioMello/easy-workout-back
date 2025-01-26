package tcc.project.easy_workout.workout.model;

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
    private List<Sets> sets;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipments_id", referencedColumnName = "id")
    private Equipments equipments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_routine_id")
    private WorkoutRoutine workoutRoutine;

    @Column(name = "completed")
    private Boolean completed;

}
