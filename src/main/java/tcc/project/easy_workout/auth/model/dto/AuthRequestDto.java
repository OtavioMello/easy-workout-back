package tcc.project.easy_workout.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequestDto {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
