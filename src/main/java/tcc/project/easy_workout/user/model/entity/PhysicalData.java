package tcc.project.easy_workout.user.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "tb_physical_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhysicalData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "imc")
    private Double imc;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

}
