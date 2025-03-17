package tcc.project.easy_workout.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import tcc.project.easy_workout.user.model.entity.PersonalTrainer;
import tcc.project.easy_workout.workout.model.dto.response.WorkoutRoutineSchemaResponseDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeRequestDto {

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

    @JsonProperty("weight")
    private Double weight;

    @JsonProperty("height")
    private Double height;

    @JsonProperty("birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("workout_routines")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<WorkoutRoutineSchemaResponseDto> workoutRoutines;

    @JsonProperty("personal_trainer")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PersonalTrainer personalTrainer;
}
