package tcc.project.easy_workout.workout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetRequestDto {

    @JsonProperty("reps")
    private Integer reps;

    @JsonProperty("weight")
    private Double weight;

}
