package tcc.project.easy_workout.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tcc.project.easy_workout.auth.utils.AuthValidation;
import tcc.project.easy_workout.user.model.dto.TraineeRequestDto;
import tcc.project.easy_workout.user.model.dto.TraineeResponseDto;
import tcc.project.easy_workout.user.model.entity.PhysicalData;
import tcc.project.easy_workout.user.model.entity.Trainee;
import tcc.project.easy_workout.user.model.entity.enums.RoleName;
import tcc.project.easy_workout.user.repository.PhysicalDataRepository;
import tcc.project.easy_workout.user.repository.RoleRepository;
import tcc.project.easy_workout.user.repository.TraineeRepository;
import tcc.project.easy_workout.user.service.TraineeService;

import jakarta.ws.rs.NotFoundException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeRepository traineeRepository;
    private final RoleRepository roleRepository;
    private final PhysicalDataRepository physicalDataRepository;
    private final ModelMapper modelMapper;

    @Override
    public URI createTrainee(TraineeRequestDto request) {
        var trainee = modelMapper.map(request, Trainee.class);
        var role = roleRepository.findByName(RoleName.ROLE_TRAINEE)
                .orElseThrow(() -> new NotFoundException("Role not found"));
        trainee.setRoles(Set.of(role));

        var physicalData = createPhysicalData(request);

        trainee.getPhysicalData().add(physicalData);
        physicalData.setTrainee(trainee);

        trainee.setPassword(new BCryptPasswordEncoder().encode(trainee.getPassword()));
        traineeRepository.save(trainee);
        LOGGER.info("[TraineeService] Trainee successfully created: id={}", trainee.getId());
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(trainee.getId()).toUri();
    }

    @Override
    public TraineeResponseDto getTraineeById(String id, String authorization) {
        AuthValidation.validateResourceAccessByAuthorizationUserId(id, authorization);
        var trainee = findTraineeById(id);
        LOGGER.info("[TraineeService] Trainee successfully found: id={}", id);
        return modelMapper.map(trainee, TraineeResponseDto.class);
    }

    @Override
    public TraineeResponseDto updateTrainee(String id, String authorization, TraineeRequestDto request) {
        AuthValidation.validateResourceAccessByAuthorizationUserId(id, authorization);
        var trainee = findTraineeById(id);
        modelMapper.map(request, trainee);

        var physicalData = updatePhysicalData(id, request);
        trainee.getPhysicalData().add(physicalData);

        traineeRepository.save(trainee);
        LOGGER.info("[TraineeService] Successfully updated trainee: id={}", id);
        return modelMapper.map(trainee, TraineeResponseDto.class);
    }

    private Trainee findTraineeById(String id) {
        LOGGER.info("[TraineeService] Searching for resource in the database: id={}", id);
        return traineeRepository.findById(id).orElseThrow(() -> new NotFoundException("Requested resource not found"));
    }

    private PhysicalData createPhysicalData(TraineeRequestDto request) {

        Double roudedImc = null;

        if (Objects.nonNull(request.getHeight()) && Objects.nonNull(request.getWeight())){
            double imc = request.getWeight() / (Math.pow(request.getHeight(), 2));
            roudedImc = Math.round(imc * 100.0) / 100.0;
        }

        return PhysicalData.builder()
                .weight(request.getWeight())
                .height(request.getHeight())
                .imc(roudedImc)
                .createdAt(LocalDate.now())
                .build();
    }

    private PhysicalData updatePhysicalData(String traineeId, TraineeRequestDto request){
        var currentDate = LocalDate.now();
        LOGGER.info("[TraineeService] Searching for resource in the database: currentDate={}, traineeId={}", currentDate, traineeId);
        var optionalCurrentPhysicalData = physicalDataRepository.findByCreatedAtAndTraineeId(currentDate, traineeId);

        if (optionalCurrentPhysicalData.isPresent()){
            var currentPhysicalData = optionalCurrentPhysicalData.get();
            var newPhysicalData = createPhysicalData(request);
            modelMapper.map(newPhysicalData, currentPhysicalData);
            return currentPhysicalData;
        }

        return createPhysicalData(request);
    }
}
