package tcc.project.easy_workout.common.exception.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomExceptionResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("details")
    private String details;

    @JsonProperty("timestamp")
    private Instant timestamp;

}
