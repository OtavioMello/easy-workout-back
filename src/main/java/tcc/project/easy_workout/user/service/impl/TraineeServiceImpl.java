package tcc.project.easy_workout.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tcc.project.easy_workout.auth.utils.AuthValidation;
import tcc.project.easy_workout.user.model.dto.TraineeDto;
import tcc.project.easy_workout.user.model.entity.Role;
import tcc.project.easy_workout.user.model.entity.Trainee;
import tcc.project.easy_workout.user.model.entity.enums.RoleName;
import tcc.project.easy_workout.user.repository.TraineeRepository;
import tcc.project.easy_workout.user.service.TraineeService;

import javax.ws.rs.NotFoundException;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeRepository traineeRepository;
    private final ModelMapper modelMapper;

    @Override
    public URI createTrainee(TraineeDto request) {
        var trainee = modelMapper.map(request, Trainee.class);
        trainee.setPassword(new BCryptPasswordEncoder().encode(trainee.getPassword()));
        trainee.getRoles().add(Role.builder().name(RoleName.ROLE_TRAINEE).build());
        traineeRepository.save(trainee);
        LOGGER.info("[TraineeService] Trainee successfully created: id={}", trainee.getId());
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(trainee.getId()).toUri();
    }

    @Override
    public TraineeDto getTraineeById(String id, String authorization) {
        AuthValidation.validateResourceAccessByAuthorizationUserId(id, authorization);
        var trainee = findTraineeById(id);
        LOGGER.info("[TraineeService] Trainee successfully found: id={}", id);
        return modelMapper.map(trainee, TraineeDto.class);
    }

    @Override
    public TraineeDto updateTrainee(String id, String authorization, TraineeDto request) {
        AuthValidation.validateResourceAccessByAuthorizationUserId(id, authorization);
        var trainee = findTraineeById(id);
        modelMapper.map(request, trainee);
        traineeRepository.save(trainee);
        LOGGER.info("[TraineeService] Successfully updated trainee: id={}", id);
        return modelMapper.map(trainee, TraineeDto.class);
    }

    private Trainee findTraineeById(String id) {
        LOGGER.info("[TraineeService] Searching for resource in the database: id={}", id);
        return traineeRepository.findById(id).orElseThrow(() -> new NotFoundException("Requested resource not found"));
    }
}
