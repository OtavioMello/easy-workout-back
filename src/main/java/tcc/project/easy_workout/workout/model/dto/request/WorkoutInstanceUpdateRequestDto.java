package tcc.project.easy_workout.workout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutInstanceUpdateRequestDto {

    @JsonProperty("sets")
    private List<SetUpdateRequestDto> sets;

    @JsonProperty("completed")
    private Boolean completed;
}
