package tcc.project.easy_workout.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import tcc.project.easy_workout.user.model.entity.PersonalTrainer;
import tcc.project.easy_workout.workout.model.WorkoutRoutine;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraineeDto {

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

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty("weight")
    private Float weight;

    @JsonProperty("height")
    private Float height;

    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("workout_workout_routines")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<WorkoutRoutine> workoutWorkoutRoutines;

    @JsonProperty("personal_trainer")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PersonalTrainer personalTrainer;
}
