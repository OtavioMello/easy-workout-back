package tcc.project.easy_workout.workout.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutRoutineInstanceResponseDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("workouts")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<WorkoutInstanceResponseDto> workouts;

    @JsonProperty("completed")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean completed;
}
