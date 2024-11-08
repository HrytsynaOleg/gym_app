package com.gym.controller;

import com.gym.dto.*;
import com.gym.dto.trainee.TraineeListItemDTO;
import com.gym.dto.trainer.TrainerCreateDTO;
import com.gym.dto.trainer.TrainerProfileDTO;
import com.gym.dto.trainer.TrainerUpdateDTO;
import com.gym.dto.trainer.TrainerUpdatedProfileDTO;
import com.gym.dto.training.TrainerTrainingListItemDTO;
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
@RequestMapping(value = "/trainers")
@RequiredArgsConstructor
@Tag(name = "Trainers", description = "Operations for creating, updating and retrieving trainers in the application")
public class TrainerController {

    @Autowired
    private final ITrainerService service;

    @PostMapping
    @Operation(summary = "Create new trainer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "New trainer successfully created",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserCredentials.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Input data for creation is not valid",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<UserCredentials> createTrainer(@RequestBody @Valid TrainerCreateDTO trainerCreateDTO) {
        TrainerModel trainer = service.createTrainer(trainerCreateDTO.getFirstName(),
                trainerCreateDTO.getLastName(), trainerCreateDTO.getSpecialization());
        UserCredentials userCredentials = UserCredentials.builder()
                .userName(trainer.getUserName())
                .password(trainer.getPassword())
                .build();
        return new ResponseEntity<>(userCredentials, HttpStatus.CREATED);
    }

    @GetMapping("/{trainer}")
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
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<TrainerProfileDTO> getTrainerProfile(@Parameter(description = "Trainer username")
                                                                @PathVariable("trainer") String userName)
            throws IncorrectCredentialException {
        TrainerModel trainerProfile = service.getTrainerProfile(userName);
        List<TraineeModel> assignedTraineeList = service.getAssignedTraineeList(userName);
        TrainerProfileDTO trainerProfileDTO = DTOMapper.mapTrainerModelToTrainerProfileDTO(trainerProfile);
        List<TraineeListItemDTO> traineeListItemDTOList = assignedTraineeList.stream()
                .map(DTOMapper::mapTraineeModelToTraineeDTO)
                .toList();
        trainerProfileDTO.setTraineeList(traineeListItemDTOList);
        return ResponseEntity.ok(trainerProfileDTO);
    }

    @PutMapping
    @Operation(summary = "Update trainer profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer profile updated",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TrainerUpdatedProfileDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Trainer not found or user unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<TrainerUpdatedProfileDTO> updateTrainerProfile(@RequestBody @Valid TrainerUpdateDTO trainerUpdateDTO)
            throws IncorrectCredentialException {
        String username = trainerUpdateDTO.getUserName();
        TrainerModel trainerProfile = service.getTrainerProfile(username);
        DTOMapper.updateTrainerModelFromDTO(trainerProfile, trainerUpdateDTO);
        TrainerModel updatedTrainerProfile = service.updateTrainerProfile(username, trainerProfile);
        List<TraineeModel> assignedTraineeList = service.getAssignedTraineeList(username);
        TrainerUpdatedProfileDTO trainerUpdatedProfileDTO =
                DTOMapper.mapTrainerModelToUpdatedTrainerProfileDTO(updatedTrainerProfile);
        List<TraineeListItemDTO> traineeListItemDTOList = assignedTraineeList.stream()
                .map(DTOMapper::mapTraineeModelToTraineeListItemDTO)
                .toList();
        trainerUpdatedProfileDTO.setTraineeList(traineeListItemDTOList);

        return ResponseEntity.ok(trainerUpdatedProfileDTO);
    }

    @GetMapping("/{trainer}/trainings")
    @Operation(summary = "Gets trainer training list")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get trainers training list success",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TrainerTrainingListItemDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Trainer not found or user unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<List<TrainerTrainingListItemDTO>> getTrainingList(
            @Parameter(description = "Trainer username")
            @PathVariable("trainer") String userName,
            @Parameter(description = "End date for training list period")
            @RequestParam(defaultValue = "", name = "periodTo") String periodTo,
            @Parameter(description = "Start date for training list period")
            @RequestParam(defaultValue = "", name = "periodFrom") String periodFrom,
            @Parameter(description = "Trainee username for training list period")
            @RequestParam(defaultValue = "", name = "trainee") String trainee
    )
            throws IncorrectCredentialException {
        List<TrainerTrainingListItemDTO> trainingList =
                service.getTrainingList(userName, periodFrom, periodTo, trainee);
        return ResponseEntity.ok(trainingList);
    }

    @PatchMapping("/{trainer}/activate")
    @Operation(summary = "Activate trainer profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer profile activated",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Trainer not found or user unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity activateTrainer(@Parameter(description = "Trainer username")
                                           @PathVariable("trainer") String userName)
            throws IncorrectCredentialException {
        service.activate(userName);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{trainer}/deactivate")
    @Operation(summary = "Deactivate trainer profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer profile deactivated",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Trainer not found or user unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity deactivateTrainer(@Parameter(description = "Trainer username")
                                             @PathVariable("trainer") String userName)
            throws IncorrectCredentialException {
        service.deactivate(userName);
        return ResponseEntity.ok().build();
    }
}