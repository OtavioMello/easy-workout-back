package tcc.project.easy_workout.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tcc.project.easy_workout.user.model.dto.PersonalTrainerDto;
import tcc.project.easy_workout.user.model.entity.PersonalTrainer;
import tcc.project.easy_workout.user.model.entity.Role;
import tcc.project.easy_workout.user.model.entity.enums.RoleName;
import tcc.project.easy_workout.user.repository.PersonalTrainerRepository;
import tcc.project.easy_workout.user.service.PersonalTrainerService;

import javax.ws.rs.NotFoundException;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class PersonalTrainerServiceImpl implements PersonalTrainerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalTrainerServiceImpl.class);
    private final PersonalTrainerRepository personalTrainerRepository;
    private final ModelMapper modelMapper;

    @Override
    public URI createPersonalTrainer(PersonalTrainerDto request) {
        var personalTrainer = modelMapper.map(request, PersonalTrainer.class);
        personalTrainer.setPassword(new BCryptPasswordEncoder().encode(personalTrainer.getPassword()));
        personalTrainer.getRoles().add(Role.builder().name(RoleName.ROLE_PERSONAL_TRAINER).build());
        personalTrainerRepository.save(personalTrainer);
        LOGGER.info("[PersonalTrainerService] Personal trainer successfully created: id={}", personalTrainer.getId());
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(personalTrainer.getId()).toUri();
    }

    @Override
    public PersonalTrainerDto getPersonalTrainerById(String id) {
        var personalTrainer = findPersonalTrainerById(id);
        LOGGER.info("[PersonalTrainerService] Personal trainer successfully found: id={}", id);
        return modelMapper.map(personalTrainer, PersonalTrainerDto.class);
    }

    @Override
    public PersonalTrainerDto updatePersonalTrainer(String id, PersonalTrainerDto request) {
        var personalTrainer = findPersonalTrainerById(id);
        modelMapper.map(request, personalTrainer);
        personalTrainerRepository.save(personalTrainer);
        LOGGER.info("[PersonalTrainerService] Successfully updated personal trainer: id={}", id);
        return modelMapper.map(personalTrainer, PersonalTrainerDto.class);
    }

    private PersonalTrainer findPersonalTrainerById(String id) {
        LOGGER.info("[PersonalTrainerService] Searching for resource in database: id={}", id);
        return personalTrainerRepository.findById(id).orElseThrow(() -> new NotFoundException("Requested resource is not found"));
    }
}
