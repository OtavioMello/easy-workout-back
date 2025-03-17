package tcc.project.easy_workout.user.service.impl;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tcc.project.easy_workout.auth.utils.AuthValidation;
import tcc.project.easy_workout.common.exception.model.ConflictException;
import tcc.project.easy_workout.user.model.dto.PersonalTrainerRequestDto;
import tcc.project.easy_workout.user.model.dto.PersonalTrainerResponseDto;
import tcc.project.easy_workout.user.model.entity.PersonalTrainer;
import tcc.project.easy_workout.user.model.entity.enums.RoleName;
import tcc.project.easy_workout.user.repository.PersonalTrainerRepository;
import tcc.project.easy_workout.user.repository.RoleRepository;
import tcc.project.easy_workout.user.repository.TraineeRepository;
import tcc.project.easy_workout.user.service.PersonalTrainerService;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalTrainerServiceImpl.class);
    public static final String REQUESTED_RESOURCE_NOT_FOUND = "Requested resource not found";
    private final PersonalTrainerRepository personalTrainerRepository;
    private final TraineeRepository traineeRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public URI createPersonalTrainer(PersonalTrainerRequestDto request) {
        var personalTrainer = modelMapper.map(request, PersonalTrainer.class);

        var role = roleRepository.findByName(RoleName.ROLE_PERSONAL_TRAINER)
                .orElseThrow(() -> new NotFoundException("Role not found"));

        personalTrainer.setRoles(Set.of(role));
        personalTrainer.setPassword(new BCryptPasswordEncoder().encode(personalTrainer.getPassword()));
        personalTrainerRepository.save(personalTrainer);
        LOGGER.info("[PersonalTrainerService] Personal trainer successfully created: id={}", personalTrainer.getId());
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(personalTrainer.getId()).toUri();
    }

    @Override
    public Void addTraineeToPersonalTrainer(String personalTrainerId, String traineeId, String authorization) {
        var trainee = traineeRepository.findById(traineeId).orElseThrow(() -> new NotFoundException(REQUESTED_RESOURCE_NOT_FOUND));
        if (Objects.nonNull(trainee.getPersonalTrainer())){
            throw new ConflictException("Current trainee already has a personal trainer");
        }

        var personalTrainer = findPersonalTrainerById(personalTrainerId);

        personalTrainer.getTrainees().add(trainee);
        trainee.setPersonalTrainer(personalTrainer);

        traineeRepository.save(trainee);

        return null;
    }

    @Override
    public PersonalTrainerResponseDto getPersonalTrainerById(String id, String authorization) {
        AuthValidation.validateResourceAccessByAuthorizationUserId(id, authorization);
        var personalTrainer = findPersonalTrainerById(id);
        LOGGER.info("[PersonalTrainerService] Personal trainer successfully found: id={}", id);
        return modelMapper.map(personalTrainer, PersonalTrainerResponseDto.class);
    }

    @Override
    public PersonalTrainerResponseDto updatePersonalTrainer(String id, String authorization, PersonalTrainerRequestDto request) {
        AuthValidation.validateResourceAccessByAuthorizationUserId(id, authorization);
        var personalTrainer = findPersonalTrainerById(id);
        modelMapper.map(request, personalTrainer);
        personalTrainerRepository.save(personalTrainer);
        LOGGER.info("[PersonalTrainerService] Successfully updated personal trainer: id={}", id);
        return modelMapper.map(personalTrainer, PersonalTrainerResponseDto.class);
    }

    private PersonalTrainer findPersonalTrainerById(String id) {
        LOGGER.info("[PersonalTrainerService] Searching for resource in the database: id={}", id);
        return personalTrainerRepository.findById(id).orElseThrow(() -> new NotFoundException(REQUESTED_RESOURCE_NOT_FOUND));
    }
}
