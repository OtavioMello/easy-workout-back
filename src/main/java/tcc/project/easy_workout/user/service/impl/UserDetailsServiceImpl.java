package tcc.project.easy_workout.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tcc.project.easy_workout.user.repository.PersonalTrainerRepository;
import tcc.project.easy_workout.user.repository.TraineeRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final PersonalTrainerRepository personalTrainerRepository;
    private final TraineeRepository traineeRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {

        var personalTrainer = personalTrainerRepository.findByEmail(username);
        var trainee = traineeRepository.findByEmail(username);

        if(personalTrainer.isPresent()) {
            return personalTrainer.get();
        }

        if(trainee.isPresent()) {
            return trainee.get();
        }

        LOGGER.warn("[UserDetails] Username not found");
        throw new UsernameNotFoundException("Username not found");
    }
}
