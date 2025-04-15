package tcc.project.easy_workout.workout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutSchemaRequestDto {

    @JsonProperty("template_id")
    private String templateId;

    @JsonProperty("sets")
    private List<SetRequestDto> sets;
}
