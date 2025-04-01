package tcc.project.easy_workout.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto {

    @JsonProperty("token")
    private String token;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("role")
    private String role;
}
