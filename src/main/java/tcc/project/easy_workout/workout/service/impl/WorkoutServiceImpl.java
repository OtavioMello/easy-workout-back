package tcc.project.easy_workout.workout.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tcc.project.easy_workout.auth.security.JsonWebTokenService;
import tcc.project.easy_workout.auth.utils.AuthValidation;
import tcc.project.easy_workout.common.exception.model.ConflictException;
import tcc.project.easy_workout.user.model.entity.Trainee;
import tcc.project.easy_workout.user.repository.TraineeRepository;
import tcc.project.easy_workout.workout.model.dto.request.*;
import tcc.project.easy_workout.workout.model.dto.response.*;
import tcc.project.easy_workout.workout.model.entity.Set;
import tcc.project.easy_workout.workout.model.entity.*;
import tcc.project.easy_workout.workout.repository.WorkoutInstanceRepository;
import tcc.project.easy_workout.workout.repository.WorkoutRoutineInstanceRepository;
import tcc.project.easy_workout.workout.repository.WorkoutRoutineSchemaRepository;
import tcc.project.easy_workout.workout.repository.WorkoutSchemaRepository;
import tcc.project.easy_workout.workout.service.WorkoutService;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkoutServiceImpl.class);
    public static final String REQUESTED_RESOURCE_NOT_FOUND = "Requested resource not found";
    private final WorkoutSchemaRepository workoutSchemaRepository;
    private final WorkoutInstanceRepository workoutInstanceRepository;
    private final TraineeRepository traineeRepository;
    private final WorkoutRoutineInstanceRepository workoutRoutineInstanceRepository;
    private final WorkoutRoutineSchemaRepository workoutRoutineSchemaRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Override
    public WorkoutSchemaResponseDto getWorkoutSchemaById(String workoutSchemaId) {
        LOGGER.info("[WorkoutService] Searching for resource in the database: workoutSchemaId={}", workoutSchemaId);
        var workoutSchema = workoutSchemaRepository.findById(workoutSchemaId).orElseThrow(() -> new NotFoundException(REQUESTED_RESOURCE_NOT_FOUND));
        LOGGER.info("[WorkoutService] Workout schema successfully found: id={}", workoutSchemaId);
        return modelMapper.map(workoutSchema, WorkoutSchemaResponseDto.class);
    }

    @Override
    public List<WorkoutSchemaResponseDto> getAllWorkoutSchemas() {
        LOGGER.info("[WorkoutService] Searching for resources in database");
        var workoutSchemas = workoutSchemaRepository.findAll();
        LOGGER.info("[WorkoutService] Found {} workout schemas in the database", workoutSchemas.size());
        return workoutSchemas.stream().map(w -> modelMapper.map(w, WorkoutSchemaResponseDto.class)).toList();
    }

    @Override
    public Void updateWorkoutInstanceById(WorkoutInstanceUpdateRequestDto request, String workoutInstanceId, String authorization) {

        LOGGER.info("[WorkoutService] Searching for resource in the database: workoutInstanceId={}", workoutInstanceId);
        var workoutInstance = workoutInstanceRepository.findById(workoutInstanceId).orElseThrow(() -> new NotFoundException(REQUESTED_RESOURCE_NOT_FOUND));

        var trainee = workoutInstance.getWorkoutRoutineInstance().getTrainee();
        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        workoutInstance.setCompleted(request.getCompleted());

        if(!CollectionUtils.isEmpty(request.getSets())){
            Map<String, Set> currentSets = workoutInstance.getSets().stream()
                    .collect(Collectors.toMap(Set::getId, Function.identity()));

            for (SetUpdateRequestDto setUpdateRequest : request.getSets()){
                if(currentSets.containsKey(setUpdateRequest.getId())){
                    Set set = currentSets.get(setUpdateRequest.getId());
                    set.setReps(setUpdateRequest.getReps());
                    set.setWeight(setUpdateRequest.getWeight());
                }
            }
        }

        workoutInstanceRepository.save(workoutInstance);

        return null;
    }

    @Override
    public Void addWorkoutRoutineSchemasToTrainee(List<WorkoutRoutineSchemaRequestDto> request, String traineeId, String authorization) {

        var trainee = findTraineeById(traineeId);
        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        List<WorkoutRoutineSchema> workoutRoutineSchemas = request.stream().map(workoutRoutineSchemaRequestDto -> {

            LOGGER.info("[WorkoutService] Searching for resources in the database: workoutSchemaIds={}", objectToJsonParser(workoutRoutineSchemaRequestDto.getWorkoutSchemaIds()));
            var workoutSchemas = workoutSchemaRepository.findByIdIn(workoutRoutineSchemaRequestDto.getWorkoutSchemaIds());

            return WorkoutRoutineSchema.builder()
                    .name(workoutRoutineSchemaRequestDto.getName())
                    .description(workoutRoutineSchemaRequestDto.getDescription())
                    .workoutSchemas(workoutSchemas)
                    .active(workoutRoutineSchemaRequestDto.getActive())
                    .daysOfWeek(workoutRoutineSchemaRequestDto.getDaysOfWeek())
                    .trainee(trainee)
                    .build();
        }).toList();

        workoutRoutineSchemaRepository.saveAll(workoutRoutineSchemas);

        LOGGER.info("[WorkoutService] Successfully added {} workout routine schemas to trainee: traineeId={}", workoutRoutineSchemas.size(), traineeId);

        return null;
    }

    @Override
    public WorkoutRoutineSchemaResponseDto getWorkoutRoutineSchemaById(String workoutRoutineSchemaId, String authorization) {
        var workoutRoutine = findWorkoutRoutineSchemaById(workoutRoutineSchemaId);
        LOGGER.info("[WorkoutService] Workout routine successfully found: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        var trainee = workoutRoutine.getTrainee();
        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);
        return modelMapper.map(workoutRoutine, WorkoutRoutineSchemaResponseDto.class);
    }

    @Override
    public List<WorkoutRoutineSchemaResponseDto> getAllTraineeWorkoutRoutineSchemas(String traineeId, String authorization) {
        AuthValidation.validateResourceAccessByAuthorizationUserId(traineeId, authorization);
        
        findTraineeById(traineeId);

        var currentDate = LocalDate.now();
        
        var workoutRoutinesSchemas = workoutRoutineSchemaRepository.findAllByTraineeId(traineeId);

        var workoutRoutinesSchemasResponse = new ArrayList<>(workoutRoutinesSchemas.stream()
                .map(workoutRoutineSchema ->
                        modelMapper.map(workoutRoutineSchema, WorkoutRoutineSchemaResponseDto.class)).toList());

        workoutRoutinesSchemasResponse.forEach(workoutRoutineSchemaResponseDto -> {

            var workoutExecutionDays = workoutRoutineSchemaResponseDto.getDaysOfWeek().split(",");

            if (Arrays.stream(workoutExecutionDays).anyMatch(workoutExecutionDay ->
                    workoutExecutionDay.equals(currentDate.getDayOfWeek().name()))){

                Collections.swap(workoutRoutinesSchemasResponse, 0, workoutRoutinesSchemasResponse.indexOf(workoutRoutineSchemaResponseDto));
                workoutRoutineSchemaResponseDto.setIsPriority(Boolean.TRUE);
            }
        });

        LOGGER.info("[WorkoutService] Found {} workout routines for trainee {} in the database", workoutRoutinesSchemasResponse.size(), traineeId);
        return workoutRoutinesSchemasResponse;
    }

    @Override
    public WorkoutRoutineSchemaResponseDto updateWorkoutRoutineSchemaById(WorkoutRoutineSchemaRequestDto request, String workoutRoutineSchemaId, String authorization) {
        var workoutRoutineSchema = findWorkoutRoutineSchemaById(workoutRoutineSchemaId);
        var trainee = workoutRoutineSchema.getTrainee();

        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);
        modelMapper.map(request, workoutRoutineSchema);
        workoutRoutineSchemaRepository.save(workoutRoutineSchema);
        LOGGER.info("[WorkoutService] Successfully updated workout routine schema: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        return modelMapper.map(workoutRoutineSchema, WorkoutRoutineSchemaResponseDto.class);
    }

    @Override
    public Void deleteWorkoutRoutineSchemaById(String workoutRoutineSchemaId, String authorization) {
        var workoutRoutineSchema = findWorkoutRoutineSchemaById(workoutRoutineSchemaId);
        var trainee = workoutRoutineSchema.getTrainee();

        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);
        workoutRoutineInstanceRepository.deleteById(workoutRoutineSchemaId);
        LOGGER.info("[WorkoutService] Successfully deleted workout routine schema: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        return null;
    }

    @Override
    public Void addWorkoutRoutineInstancesToTrainee(List<WorkoutRoutineInstanceRequestDto> request, String traineeId, String authorization) {

        var trainee = findTraineeById(traineeId);
        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        List<WorkoutRoutineInstance> workoutRoutineInstances = request.stream()
                .map(workoutRoutineInstanceRequestDto -> createWorkoutRoutineInstance(workoutRoutineInstanceRequestDto, trainee)).toList();

        workoutRoutineInstanceRepository.saveAll(workoutRoutineInstances);
        LOGGER.info("[WorkoutService] Successfully added {} workout routine instances to trainee: traineeId={}", workoutRoutineInstances.size(), traineeId);
        return null;
    }

    @Override
    public List<WorkoutRoutineInstanceResponseDto> getAllTraineeWorkoutRoutineInstances(String traineeId, String authorization) {

        var trainee = findTraineeById(traineeId);
        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        var workoutRoutines = workoutRoutineInstanceRepository.findAllByTraineeId(traineeId);

        return workoutRoutines.stream().map(workoutRoutineInstance -> {

            var workoutRoutineSchema = workoutRoutineInstance.getSchema();
            var workoutInstances = workoutRoutineInstance.getWorkoutInstances();

            var workoutInstancesResponse = workoutInstances.stream()
                    .map(this::toWorkoutInstanceResponseDto).toList();

            return toWorkoutRoutineInstanceResponseDto(workoutRoutineInstance, workoutRoutineSchema, workoutInstancesResponse);

        }).toList();
    }

    @Override
    public WorkoutRoutineInstanceResponseDto getWorkoutRoutineInstanceById(String workoutRoutineInstanceId, String authorization) {

        var workoutRoutineInstance = findWorkoutRoutineInstanceById(workoutRoutineInstanceId);
        var trainee = workoutRoutineInstance.getTrainee();

        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        var workoutRoutineSchema = workoutRoutineInstance.getSchema();
        var workoutInstances = workoutRoutineInstance.getWorkoutInstances();

        var workoutInstancesResponse = workoutInstances.stream().map(this::toWorkoutInstanceResponseDto).toList();

        return toWorkoutRoutineInstanceResponseDto(workoutRoutineInstance, workoutRoutineSchema, workoutInstancesResponse);
    }

    @Override
    public Void updateWorkoutRoutineInstanceById(String workoutRoutineInstanceId, String authorization) {

        var workoutRoutineInstance = findWorkoutRoutineInstanceById(workoutRoutineInstanceId);
        var trainee = workoutRoutineInstance.getTrainee();

        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        var workoutInstances = workoutRoutineInstance.getWorkoutInstances();

        var isAllWorkoutInstancesCompleted = workoutInstances.stream()
                .allMatch(workoutInstance -> workoutInstance.getCompleted().equals(Boolean.TRUE));

        if(!isAllWorkoutInstancesCompleted){
            LOGGER.error("There are a conflict with uncompleted workouts: workoutRoutineInstanceId={}", workoutRoutineInstanceId);
            throw new ConflictException("There are a conflict with uncompleted workouts");
        }

        workoutRoutineInstance.setCompleted(Boolean.TRUE);
        workoutRoutineInstanceRepository.save(workoutRoutineInstance);

        return null;
    }

    private WorkoutRoutineSchema findWorkoutRoutineSchemaById(String workoutRoutineSchemaId) {
        LOGGER.info("[WorkoutService] Searching for resources in the database: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        return workoutRoutineSchemaRepository.findById(workoutRoutineSchemaId).orElseThrow(() -> new NotFoundException(REQUESTED_RESOURCE_NOT_FOUND));
    }

    private Trainee findTraineeById(String traineeId) {
        LOGGER.info("[WorkoutService] Searching for resources in the database: traineeId={}", traineeId);
        return traineeRepository.findById(traineeId).orElseThrow(() -> new NotFoundException(REQUESTED_RESOURCE_NOT_FOUND));
    }

    private WorkoutRoutineInstance findWorkoutRoutineInstanceById(String workoutRoutineInstanceId){
        LOGGER.info("[WorkoutService] Searching for resource in the database: workoutRoutineInstanceId={}", workoutRoutineInstanceId);
        return workoutRoutineInstanceRepository.findById(workoutRoutineInstanceId).orElseThrow(() -> new NotFoundException(REQUESTED_RESOURCE_NOT_FOUND));
    }

    private void validateIfAuthenticatedUserCanPerformAction(String authorization, Trainee trainee) {

        var formattedToken = authorization.replace("Bearer ", "");
        var userId = JsonWebTokenService.getUserId(formattedToken);
        var personalTrainerId = Objects.nonNull(trainee.getPersonalTrainer()) ? trainee.getPersonalTrainer().getId() : "";

        if (trainee.getId().equals(userId) || personalTrainerId.equals(userId)){
            return;
        }

        LOGGER.error("[WorkoutService] Authenticated user can't perform this action");
        throw new ForbiddenException("Authenticated user can't perform this action");
    }

    private String objectToJsonParser(Object object) {

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException(e);
        }
    }

    private WorkoutRoutineInstance createWorkoutRoutineInstance(WorkoutRoutineInstanceRequestDto workoutRoutineInstanceRequestDto, Trainee trainee){
        var workoutRoutineSchema = findWorkoutRoutineSchemaById(workoutRoutineInstanceRequestDto.getSchemaId());

        List<WorkoutInstance> workoutInstances = workoutRoutineSchema.getWorkoutSchemas().stream()
                .map(workoutSchema -> createWorkoutInstance(workoutSchema, workoutRoutineInstanceRequestDto)).toList();

        var workoutRoutineInstance = WorkoutRoutineInstance.builder()
                .schema(workoutRoutineSchema)
                .workoutInstances(workoutInstances)
                .completed(workoutRoutineInstanceRequestDto.getCompleted())
                .trainee(trainee)
                .build();

        workoutInstances.forEach(workoutInstance -> workoutInstance.setWorkoutRoutineInstance(workoutRoutineInstance));

        return workoutRoutineInstance;
    }

    private WorkoutInstance createWorkoutInstance(WorkoutSchema workoutSchema, WorkoutRoutineInstanceRequestDto workoutRoutineInstanceRequestDto){
        var sets = createSetsForWorkout(workoutSchema.getId(), workoutRoutineInstanceRequestDto.getWorkoutSets());

        var workoutInstance = WorkoutInstance.builder()
                .schema(workoutSchema)
                .sets(sets)
                .completed(Boolean.FALSE)
                .build();

        sets.forEach(set -> set.setWorkoutInstance(workoutInstance));
        return workoutInstance;
    }

    private List<Set> createSetsForWorkout(String workoutSchemaId, List<WorkoutSetsRequestDto> workoutSetsRequestDto) {
        return Optional.ofNullable(workoutSetsRequestDto)
                .orElse(Collections.emptyList()).stream()
                .filter(workoutSetRequestDto -> workoutSetRequestDto.getWorkoutId().equals(workoutSchemaId))
                .flatMap(workoutSetRequestDto -> Optional.ofNullable(workoutSetRequestDto.getSets())
                        .orElse(Collections.emptyList()).stream()
                        .map(set -> modelMapper.map(set, Set.class)))
                .toList();
    }


    private WorkoutRoutineInstanceResponseDto toWorkoutRoutineInstanceResponseDto(WorkoutRoutineInstance workoutRoutineInstance, WorkoutRoutineSchema workoutRoutineSchema, List<WorkoutInstanceResponseDto> workoutInstancesResponse){
        return WorkoutRoutineInstanceResponseDto.builder()
                .id(workoutRoutineInstance.getId())
                .name(workoutRoutineSchema.getName())
                .description(workoutRoutineSchema.getDescription())
                .workouts(workoutInstancesResponse)
                .completed(workoutRoutineInstance.getCompleted())
                .build();
    }

    private WorkoutInstanceResponseDto toWorkoutInstanceResponseDto(WorkoutInstance workoutInstance){

        var workoutSchema = workoutInstance.getSchema();
        var sets = toSetResponseDtoList(workoutInstance.getSets());

        return WorkoutInstanceResponseDto.builder()
                .id(workoutInstance.getId())
                .name(workoutSchema.getName())
                .description(workoutSchema.getDescription())
                .equipment(modelMapper.map(workoutSchema.getEquipment(), EquipmentResponseDto.class))
                .sets(sets)
                .completed(workoutInstance.getCompleted())
                .build();
    }

    private List<SetResponseDto> toSetResponseDtoList(List<Set> sets){

        return Optional.ofNullable(sets)
                .orElse(Collections.emptyList())
                .stream().map(this::toSetResponseDto)
                .toList();
    }

    private SetResponseDto toSetResponseDto(Set set){
        return SetResponseDto.builder()
                .id(set.getId())
                .reps(set.getReps())
                .weight(set.getWeight())
                .build();
    }
}
