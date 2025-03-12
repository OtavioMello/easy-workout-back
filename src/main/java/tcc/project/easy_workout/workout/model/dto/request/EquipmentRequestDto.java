package tcc.project.easy_workout.workout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentRequestDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    @JsonProperty("name")
    private String name;
}
