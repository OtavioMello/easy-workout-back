package tcc.project.easy_workout.workout.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutRoutineSchemaResponseDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonProperty("workouts")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<WorkoutSchemaResponseDto> workoutSchemas;

    @JsonProperty("active")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean active;

    @JsonProperty("days_of_week")
    private String daysOfWeek;

    @JsonProperty("is_priority")
    private Boolean isPriority = Boolean.FALSE;

    @JsonProperty("tags")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private HashSet<String> tags;

}
