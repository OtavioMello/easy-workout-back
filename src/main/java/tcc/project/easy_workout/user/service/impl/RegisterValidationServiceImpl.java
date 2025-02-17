package tcc.project.easy_workout.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tcc.project.easy_workout.user.model.dto.RegisterValidationDto;
import tcc.project.easy_workout.user.repository.PersonalTrainerRepository;
import tcc.project.easy_workout.user.repository.TraineeRepository;
import tcc.project.easy_workout.user.service.RegisterValidationService;

@Service
@RequiredArgsConstructor
public class RegisterValidationServiceImpl implements RegisterValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterValidationServiceImpl.class);
    private final TraineeRepository traineeRepository;
    private final PersonalTrainerRepository personalTrainerRepository;

    @Override
    public RegisterValidationDto validateEmail(String email) {

        LOGGER.info("[RegisterValidationService] validateEmail - Searching for trainee");
        var trainee = traineeRepository.findByEmail(email);
        LOGGER.info("[RegisterValidationService] validateEmail - Searching for personal trainer");
        var personalTrainer = personalTrainerRepository.findByEmail(email);

        var alreadyExists = trainee.isPresent() || personalTrainer.isPresent();

        return new RegisterValidationDto(alreadyExists);
    }

    @Override
    public RegisterValidationDto validateNickname(String nickname) {

        LOGGER.info("[RegisterValidationService] validateNickname - Searching for trainee");
        var trainee = traineeRepository.findByNickname(nickname);
        LOGGER.info("[RegisterValidationService] validateNickname - Searching for personal trainer");
        var personalTrainer = personalTrainerRepository.findByNickname(nickname);

        var alreadyExists = trainee.isPresent() || personalTrainer.isPresent();

        return new RegisterValidationDto(alreadyExists);
    }
}
