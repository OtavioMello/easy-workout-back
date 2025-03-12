package tcc.project.easy_workout.workout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutSetsRequestDto {

    @JsonProperty("workout_id")
    private String workoutId;

    @JsonProperty("sets")
    private List<SetRequestDto> sets = new ArrayList<>();
}
