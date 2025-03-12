package tcc.project.easy_workout.user.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcc.project.easy_workout.user.model.dto.PersonalTrainerDto;
import tcc.project.easy_workout.user.service.PersonalTrainerService;

import java.net.URI;

@RestController
@RequestMapping("/personal-trainers")
@RequiredArgsConstructor
public class PersonalTrainerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalTrainerController.class);
    private final PersonalTrainerService personalTrainerService;

    @PostMapping
    public ResponseEntity<URI> createPersonalTrainer(@RequestBody PersonalTrainerDto request) {
        LOGGER.info("[PersonalTrainerController] Calling createPersonalTrainer");
        return ResponseEntity.ok(personalTrainerService.createPersonalTrainer(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalTrainerDto> getPersonalTrainerById(@PathVariable String id, @RequestHeader("Authorization") String authorization) {
        LOGGER.info("[PersonalTrainerController] Calling getPersonalTrainerById: id={}", id);
        return ResponseEntity.ok(personalTrainerService.getPersonalTrainerById(id, authorization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalTrainerDto> updatePersonalTrainer(@PathVariable String id, @RequestHeader("Authorization") String authorization, @RequestBody PersonalTrainerDto request) {
        LOGGER.info("[PersonalTrainerController] Calling updatePersonalTrainer: id={}", id);
        return ResponseEntity.ok(personalTrainerService.updatePersonalTrainer(id, authorization, request));
    }
}
