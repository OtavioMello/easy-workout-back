package tcc.project.easy_workout.workout.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutSchemaResponseDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("equipment")
    private EquipmentResponseDto equipment;

}
