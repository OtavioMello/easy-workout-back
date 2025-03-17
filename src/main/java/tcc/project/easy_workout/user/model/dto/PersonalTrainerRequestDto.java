package tcc.project.easy_workout.user.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalTrainerRequestDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("email")
    private String email;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
