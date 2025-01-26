package tcc.project.easy_workout.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcc.project.easy_workout.user.model.dto.PersonalTrainerDto;
import tcc.project.easy_workout.user.service.PersonalTrainerService;

import java.net.URI;

@RestController
@RequestMapping("/personal-trainer")
@RequiredArgsConstructor
public class PersonalTrainerController {

    private final PersonalTrainerService personalTrainerService;

    @PostMapping
    public ResponseEntity<URI> createPersonalTrainer(@RequestBody PersonalTrainerDto request) {
        return ResponseEntity.ok(personalTrainerService.createPersonalTrainer(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonalTrainerDto> getPersonalTrainerById(@PathVariable String id, @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(personalTrainerService.getPersonalTrainerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalTrainerDto> updatePersonalTrainer(@PathVariable String id, @RequestHeader("Authorization") String authorization, @RequestBody PersonalTrainerDto request) {
        return ResponseEntity.ok(personalTrainerService.updatePersonalTrainer(id, request));
    }
}
