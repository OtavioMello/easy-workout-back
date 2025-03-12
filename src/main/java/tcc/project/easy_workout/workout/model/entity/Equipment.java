package tcc.project.easy_workout.workout.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity( name = "tb_equipments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

}
