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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Trainer", description = "Operations for creating, updating, retrieving and deleting trainers in the application")
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
    @Operation(summary = "Gets trainer profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the trainer",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TrainerProfileDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Trainer not found or user unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserCredentials.class)))
                    })
    })
    private ResponseEntity<TrainerProfileDTO> getTrainerProfile(@Parameter(description = "User name")
                                                                    @PathVariable("user") String userName,
                                                                @Parameter(description = "User password")
                                                                @RequestHeader("password") String password)
            throws IncorrectCredentialException {
        UserCredentials credentials = UserCredentials.builder()
                .userName(userName)
                .password(password)
                .build();
        TrainerModel trainerProfile = service.getTrainerProfile(credentials);
        List<TraineeModel> assignedTraineeList = service.getAssignedTraineeList(credentials);
        TrainerProfileDTO trainerProfileDTO = DTOMapper.mapTrainerModelToTrainerProfileDTO(trainerProfile);
        List<TraineeDTO> traineeDTOList = assignedTraineeList.stream()
                .map(DTOMapper::mapTraineeModelToTraineeDTO)
                .toList();
        trainerProfileDTO.setTraineeList(traineeDTOList);
        return ResponseEntity.ok(trainerProfileDTO);
    }
}

