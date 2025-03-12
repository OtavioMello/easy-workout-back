package tcc.project.easy_workout.workout.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutInstanceResponseDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("equipment")
    private EquipmentResponseDto equipment;

    @JsonProperty("sets")
    private List<SetResponseDto> sets = new ArrayList<>();

    @Column(name = "completed")
    private Boolean completed;
}
