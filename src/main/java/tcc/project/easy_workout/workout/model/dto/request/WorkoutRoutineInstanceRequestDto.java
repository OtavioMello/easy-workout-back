package tcc.project.easy_workout.workout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutRoutineInstanceRequestDto {

    @JsonProperty("schema_id")
    private String schemaId;

    @JsonProperty("workout_sets")
    private List<WorkoutSetsRequestDto> workoutSets;

    @JsonProperty("completed")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean completed;
}
