package tcc.project.easy_workout.workout.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ForbiddenException;
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
import tcc.project.easy_workout.workout.model.dto.request.SetUpdateRequestDto;
import tcc.project.easy_workout.workout.model.dto.request.WorkoutInstanceUpdateRequestDto;
import tcc.project.easy_workout.workout.model.dto.request.WorkoutRoutineSchemaRequestDto;
import tcc.project.easy_workout.workout.model.dto.response.*;
import tcc.project.easy_workout.workout.model.entity.Set;
import tcc.project.easy_workout.workout.model.entity.*;
import tcc.project.easy_workout.workout.repository.WorkoutInstanceRepository;
import tcc.project.easy_workout.workout.repository.WorkoutRoutineInstanceRepository;
import tcc.project.easy_workout.workout.repository.WorkoutRoutineSchemaRepository;
import tcc.project.easy_workout.workout.repository.WorkoutTemplateRepository;
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
    private final WorkoutInstanceRepository workoutInstanceRepository;
    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final TraineeRepository traineeRepository;
    private final WorkoutRoutineInstanceRepository workoutRoutineInstanceRepository;
    private final WorkoutRoutineSchemaRepository workoutRoutineSchemaRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Override
    public WorkoutTemplateResponseDto getWorkoutTemplateById(String workoutTemplateId) {
        var workoutTemplate = findWorkoutTemplateById(workoutTemplateId);
        LOGGER.info("[WorkoutService] Workout schema successfully found: id={}", workoutTemplateId);
        return modelMapper.map(workoutTemplate, WorkoutTemplateResponseDto.class);
    }

    @Override
    public List<WorkoutTemplateResponseDto> getAllWorkoutTemplates() {
        LOGGER.info("[WorkoutService] Searching for resources in database");
        var workoutTemplates = workoutTemplateRepository.findAll();
        LOGGER.info("[WorkoutService] Found {} workout schemas in the database", workoutTemplates.size());
        return workoutTemplates.stream().map(w -> modelMapper.map(w, WorkoutTemplateResponseDto.class)).toList();
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
        LOGGER.info("[WorkoutService] Successfully updated workout instance: workoutInstanceId={}", workoutInstanceId);
        return null;
    }

    @Override
    public Void addWorkoutRoutineSchemasToTrainee(List<WorkoutRoutineSchemaRequestDto> request, String traineeId, String authorization) {
        var trainee = findTraineeById(traineeId);
        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        List<WorkoutRoutineSchema> workoutRoutineSchemas = request.stream().map(workoutRoutineSchemaRequestDto -> {

            List<WorkoutSchema> workoutSchemas = new ArrayList<>();

            workoutRoutineSchemaRequestDto.getWorkoutSchemas().forEach(workoutSchemaRequestDto -> {
                var workoutTemplate = findWorkoutTemplateById(workoutSchemaRequestDto.getTemplateId());
                var workoutSchema = WorkoutSchema.builder()
                        .template(workoutTemplate)
                        .sets(workoutSchemaRequestDto.getSets().stream()
                                .map(setRequestDto ->
                                        modelMapper.map(setRequestDto, SetSchema.class))
                                .toList())
                        .build();

                workoutSchema.getSets().forEach(setSchema -> setSchema.setWorkoutSchema(workoutSchema));
                workoutSchemas.add(workoutSchema);
            });

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
        var workoutRoutineSchemaResponse = modelMapper.map(workoutRoutine, WorkoutRoutineSchemaResponseDto.class);
        setWorkoutRoutineExecutionPriority(workoutRoutineSchemaResponse, null);
        return workoutRoutineSchemaResponse;
    }

    @Override
    public List<WorkoutRoutineSchemaResponseDto> getAllTraineeWorkoutRoutineSchemas(String traineeId, String authorization) {
        AuthValidation.validateResourceAccessByAuthorizationUserId(traineeId, authorization);
        
        findTraineeById(traineeId);
        LOGGER.info("[WorkoutService] Searching for resource in the database: traineeId={}", traineeId);
        var workoutRoutinesSchemas = workoutRoutineSchemaRepository.findAllByTraineeId(traineeId);

        var workoutRoutinesSchemasResponse = new ArrayList<>(workoutRoutinesSchemas.stream()
                .map(workoutRoutineSchema ->
                        modelMapper.map(workoutRoutineSchema, WorkoutRoutineSchemaResponseDto.class)).toList());

        workoutRoutinesSchemasResponse.forEach(workoutRoutineSchemaResponseDto -> {

            workoutRoutineSchemaResponseDto.setTags(workoutRoutineSchemaResponseDto.getWorkoutSchemas().stream()
                    .map(workoutSchemaResponseDto -> workoutSchemaResponseDto.getTemplate().getTag())
                    .collect(Collectors.toCollection(HashSet::new)));

            setWorkoutRoutineExecutionPriority(workoutRoutineSchemaResponseDto, workoutRoutinesSchemasResponse);

        });

        LOGGER.info("[WorkoutService] Successfully found {} workout routines for trainee {} in the database", workoutRoutinesSchemasResponse.size(), traineeId);
        return workoutRoutinesSchemasResponse;
    }

    @Override
    public WorkoutRoutineInstanceResponseDto getWorkoutRoutineInstanceBySchemaId(String workoutRoutineSchemaId, String authorization) {

        LOGGER.info("[WorkoutService] Searching for workout routine instance in the database: workoutRoutineSchemaId={}", workoutRoutineSchemaId);
        var optionalWorkoutRoutineInstance = workoutRoutineInstanceRepository.findBySchemaId(workoutRoutineSchemaId);

        if (optionalWorkoutRoutineInstance.isPresent() && Boolean.FALSE.equals(optionalWorkoutRoutineInstance.get().getCompleted())){

            var trainee = optionalWorkoutRoutineInstance.get().getTrainee();
            validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

            var workoutRoutineInstance = optionalWorkoutRoutineInstance.get();

            LOGGER.info("[WorkoutService] Successfully found an existing workout routine instance for workout routine schema in the database: workoutRoutineInstanceId={}", workoutRoutineInstance.getId());
            return WorkoutRoutineInstanceResponseDto.builder().id(workoutRoutineInstance.getId()).build();
        }

        var formattedToken = authorization.replace("Bearer ", "");
        var userId = JsonWebTokenService.getUserId(formattedToken);
        var trainee = findTraineeById(userId);
        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        var workoutRoutineInstance = createWorkoutRoutineInstance(workoutRoutineSchemaId);
        workoutRoutineInstanceRepository.save(workoutRoutineInstance);

        LOGGER.info("[WorkoutService] Successfully created a new workout routine instance: workoutRoutineInstanceId={}", workoutRoutineInstance.getId());
        return WorkoutRoutineInstanceResponseDto.builder().id(workoutRoutineInstance.getId()).build();

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
    public List<WorkoutRoutineInstanceResponseDto> getAllTraineeWorkoutRoutineInstances(String traineeId, String authorization) {
        var trainee = findTraineeById(traineeId);
        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);
        LOGGER.info("[WorkoutService] Searching for workout routine instance(s) in the database: traineeId={}", traineeId);
        var workoutRoutines = workoutRoutineInstanceRepository.findAllByTraineeId(traineeId);

        LOGGER.info("[WorkoutService] Successfully found {} workout routine instance(s) for trainee {} in the database", workoutRoutines.size(), traineeId);
        return workoutRoutines.stream().map(this::toWorkoutRoutineInstanceResponseDto).toList();
    }

    @Override
    public WorkoutRoutineInstanceResponseDto getWorkoutRoutineInstanceById(String workoutRoutineInstanceId, String authorization) {

        LOGGER.info("[WorkoutService] Searching for workout routine instance in the database: workoutRoutineInstanceId={}", workoutRoutineInstanceId);
        var workoutRoutineInstance = findWorkoutRoutineInstanceById(workoutRoutineInstanceId);
        var trainee = workoutRoutineInstance.getTrainee();


        validateIfAuthenticatedUserCanPerformAction(authorization, trainee);

        LOGGER.info("[WorkoutService] Successfully found workout routine instance in the database: workoutRoutineInstanceId={}", workoutRoutineInstanceId);
        return toWorkoutRoutineInstanceResponseDto(workoutRoutineInstance);
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

        LOGGER.info("[WorkoutService] Successfully updated workout routine instance: workoutRoutineInstanceId={}", workoutRoutineInstanceId);
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

    private WorkoutTemplate findWorkoutTemplateById(String workoutTemplateId){
        LOGGER.info("[WorkoutService] Searching for resource in the database: workoutTemplateId={}", workoutTemplateId);
        return workoutTemplateRepository.findById(workoutTemplateId).orElseThrow(() -> new NotFoundException(REQUESTED_RESOURCE_NOT_FOUND));
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

    private WorkoutRoutineInstance createWorkoutRoutineInstance(String workoutRoutineSchemaId){

        var workoutRoutineSchema = findWorkoutRoutineSchemaById(workoutRoutineSchemaId);

        List<WorkoutInstance> workoutInstances = workoutRoutineSchema.getWorkoutSchemas().stream()
                .map(this::createWorkoutInstance).toList();

        var workoutRoutineInstance = WorkoutRoutineInstance.builder()
                .schema(workoutRoutineSchema)
                .workoutInstances(workoutInstances)
                .completed(Boolean.FALSE)
                .trainee(workoutRoutineSchema.getTrainee())
                .build();

        workoutInstances.forEach(workoutInstance -> workoutInstance.setWorkoutRoutineInstance(workoutRoutineInstance));

        return workoutRoutineInstance;
    }

    private WorkoutInstance createWorkoutInstance(WorkoutSchema workoutSchema){
        var sets = createSetsForWorkout(workoutSchema.getSets());

        var workoutInstance = WorkoutInstance.builder()
                .schema(workoutSchema)
                .sets(sets)
                .completed(Boolean.FALSE)
                .build();

        sets.forEach(set -> set.setWorkoutInstance(workoutInstance));
        return workoutInstance;
    }

    private List<Set> createSetsForWorkout(List<SetSchema> setSchemas) {
        return setSchemas.stream().map(setSchema -> {
            return Set.builder()
                    .reps(setSchema.getReps())
                    .weight(0.0)
                    .build();
        }).toList();
    }

    private WorkoutRoutineInstanceResponseDto toWorkoutRoutineInstanceResponseDto(WorkoutRoutineInstance workoutRoutineInstance){

        var workoutRoutineSchema = workoutRoutineInstance.getSchema();

        var workoutInstancesResponse = workoutRoutineInstance.getWorkoutInstances().stream()
                .map(this::toWorkoutInstanceResponseDto).toList();

        return WorkoutRoutineInstanceResponseDto.builder()
                .id(workoutRoutineInstance.getId())
                .name(workoutRoutineSchema.getName())
                .description(workoutRoutineSchema.getDescription())
                .workouts(workoutInstancesResponse)
                .completed(workoutRoutineInstance.getCompleted())
                .build();
    }

    private WorkoutInstanceResponseDto toWorkoutInstanceResponseDto(WorkoutInstance workoutInstance){

        var workoutTemplate = workoutInstance.getSchema().getTemplate();
        var sets = toSetResponseDtoList(workoutInstance.getSets());

        return WorkoutInstanceResponseDto.builder()
                .id(workoutInstance.getId())
                .name(workoutTemplate.getName())
                .description(workoutTemplate.getDescription())
                .equipment(modelMapper.map(workoutTemplate.getEquipment(), EquipmentResponseDto.class))
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

    private void setWorkoutRoutineExecutionPriority(WorkoutRoutineSchemaResponseDto workoutRoutineSchemaResponse, List<WorkoutRoutineSchemaResponseDto> workoutRoutineSchemasResponseList){

        var currentDate = LocalDate.now().getDayOfWeek().name();
        var workoutExecutionDays = Arrays.asList(workoutRoutineSchemaResponse.getDaysOfWeek().split(","));

        if (workoutExecutionDays.contains(currentDate)){
            workoutRoutineSchemaResponse.setIsPriority(Boolean.TRUE);

            if (Objects.nonNull(workoutRoutineSchemasResponseList)){
                Collections.swap(workoutRoutineSchemasResponseList, 0, workoutRoutineSchemasResponseList.indexOf(workoutRoutineSchemaResponse));
            }
        }
    }
}
