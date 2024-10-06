package com.gym.controller;

import com.gym.dto.TraineeDTO;
import com.gym.dto.TrainerCreateDTO;
import com.gym.dto.TrainerProfileDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.UserCredentials;
import com.gym.service.ITrainerService;
import com.gym.utils.DTOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/trainer")
@RequiredArgsConstructor
public class TrainerController {

    @Autowired
    private final ITrainerService service;

    @PostMapping
    private ResponseEntity<UserCredentials> createTrainer(@RequestBody @Valid TrainerCreateDTO trainerCreateDTO) {
        TrainerModel trainer = service.createTrainer(trainerCreateDTO.getFirstName(),
                trainerCreateDTO.getLastName(), trainerCreateDTO.getSpecialization());
        UserCredentials userCredentials = UserCredentials.builder()
                .userName(trainer.getUserName())
                .password(trainer.getPassword())
                .build();
        return new ResponseEntity<>(userCredentials, HttpStatus.CREATED);
    }

    @GetMapping("/{user}")
    private ResponseEntity<TrainerProfileDTO> getTrainerProfile(@PathVariable("user") String userName,
                                                                @RequestHeader("password") String password) {
        UserCredentials credentials = UserCredentials.builder()
                .userName(userName)
                .password(password)
                .build();
        TrainerModel trainerProfile;
        List<TraineeModel> assignedTraineeList;
        try {
            trainerProfile = service.getTrainerProfile(credentials);
            assignedTraineeList = service.getAssignedTraineeList(credentials);
        } catch (IncorrectCredentialException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        TrainerProfileDTO trainerProfileDTO = DTOMapper.mapTrainerModelToTrainerProfileDTO(trainerProfile);
        List<TraineeDTO> traineeDTOList = assignedTraineeList.stream()
                .map(DTOMapper::mapTraineeModelToTraineeDTO)
                .toList();
        trainerProfileDTO.setTraineeList(traineeDTOList);
        return ResponseEntity.ok(trainerProfileDTO);
    }
}

