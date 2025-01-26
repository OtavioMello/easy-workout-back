package tcc.project.easy_workout.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tcc.project.easy_workout.user.repository.PersonalTrainerRepository;
import tcc.project.easy_workout.user.repository.TraineeRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonalTrainerRepository personalTrainerRepository;
    private final TraineeRepository traineeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var personalTrainer = personalTrainerRepository.findByEmail(username);
        var trainee = traineeRepository.findByEmail(username);

        if(Objects.nonNull(personalTrainer) ) {
            return personalTrainer;
        }

        if(Objects.nonNull(trainee)) {
            return trainee;
        }

        throw new UsernameNotFoundException("User not found");
    }
}
