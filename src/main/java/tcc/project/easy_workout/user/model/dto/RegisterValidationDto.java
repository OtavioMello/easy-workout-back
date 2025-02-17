package tcc.project.easy_workout.user.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterValidationDto {

    @JsonProperty("exists")
    private Boolean exists;
}
