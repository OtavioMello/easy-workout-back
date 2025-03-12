package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;
import tcc.project.easy_workout.user.model.entity.Trainee;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_workout_routine_schemas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutRoutineSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "workout_routine_schema_workouts",
            joinColumns = @JoinColumn(name = "workout_routine_schema_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id"))
    private List<WorkoutSchema> workoutSchemas = new ArrayList<>();

    @Column(name = "active")
    private Boolean active;

    @Column(name = "days_of_week")
    private String daysOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

}
