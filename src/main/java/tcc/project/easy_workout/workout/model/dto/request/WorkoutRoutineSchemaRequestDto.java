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
public class WorkoutRoutineSchemaRequestDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonProperty("workout_schemas")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<WorkoutSchemaRequestDto> workoutSchemas;

    @JsonProperty("active")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean active;

    @JsonProperty("days_of_week")
    private String daysOfWeek;

}
