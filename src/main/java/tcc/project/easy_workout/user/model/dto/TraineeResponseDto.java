package tcc.project.easy_workout.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import tcc.project.easy_workout.workout.model.dto.response.WorkoutRoutineInstanceResponseDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeResponseDto {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("workout_routines")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<WorkoutRoutineInstanceResponseDto> workoutRoutines;

    @JsonProperty("physical_data")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PhysicalDataResponseDto> physicalData;

    @JsonProperty("personal_trainer")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PersonalTrainerResponseDto personalTrainer;
}
