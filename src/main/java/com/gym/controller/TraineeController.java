package com.gym.controller;

import com.gym.dto.*;
import com.gym.dto.trainee.*;
import com.gym.dto.trainer.TrainerListDTO;
import com.gym.dto.trainer.TrainerListItemDTO;
import com.gym.dto.training.TraineeTrainingListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TraineeModel;
import com.gym.model.TrainerModel;
import com.gym.model.UserCredentials;
import com.gym.service.ITraineeService;
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
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/trainees")
@RequiredArgsConstructor
@Tag(name = "Trainees", description = "Operations for creating, updating, retrieving and deleting trainees in the application")
public class TraineeController {

    @Autowired
    private final ITraineeService service;

    @PostMapping
    @Operation(summary = "Create new trainee")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "New trainee successfully created",
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
    private ResponseEntity<UserCredentials> createTrainee(@RequestBody @Valid TraineeCreateDTO traineeCreateDTO) {
        TraineeModel trainee = service.createTrainee(traineeCreateDTO.getFirstName(),
                traineeCreateDTO.getLastName(), traineeCreateDTO.getAddress(), traineeCreateDTO.getDateOfBirth());
        UserCredentials userCredentials = UserCredentials.builder()
                .userName(trainee.getUserName())
                .password(trainee.getPassword())
                .build();
        return new ResponseEntity<>(userCredentials, HttpStatus.CREATED);
    }

    @GetMapping("/{trainee}")
    @Operation(summary = "Gets trainee profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the trainee",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TraineeProfileDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied for this user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<TraineeProfileDTO> getTraineeProfile(@Parameter(description = "Trainee username")
                                                                @PathVariable("trainee") String username)
            throws NoSuchElementException {
        TraineeModel traineeProfile = service.getTraineeProfile(username);
        List<TrainerModel> assignedTrainerList = service.getAssignedTrainerList(username);
        TraineeProfileDTO traineeProfileDTO = DTOMapper.mapTraineeModelToTraineeProfileDTO(traineeProfile);
        List<TrainerListItemDTO> trainerListItemDTOList = assignedTrainerList.stream()
                .map(DTOMapper::mapTrainerModelToTrainerListItemDTO)
                .toList();
        traineeProfileDTO.setTrainerList(trainerListItemDTOList);
        return ResponseEntity.ok(traineeProfileDTO);
    }

    @PutMapping("/{trainee}/update")
    @Operation(summary = "Update trainee profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainee profile updated",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TraineeUpdatedProfileDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied for this user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<TraineeUpdatedProfileDTO> updateTraineeProfile(@RequestBody @Valid TraineeUpdateDTO traineeUpdateDTO,
                                                                          @Parameter(description = "Trainee username")
                                                                          @PathVariable("trainee") String username)
            throws IncorrectCredentialException {
        TraineeModel traineeProfile = service.getTraineeProfile(username);
        DTOMapper.updateTraineeModelFromDTO(traineeProfile, traineeUpdateDTO);
        TraineeModel updatedTraineeProfile = service.update(username, traineeProfile);
        List<TrainerModel> assignedTrainerList = service.getAssignedTrainerList(username);
        TraineeUpdatedProfileDTO traineeUpdatedProfileDTO =
                DTOMapper.mapTraineeModelToUpdatedTraineeProfileDTO(updatedTraineeProfile);
        List<TrainerListItemDTO> trainerListItemDTOList = assignedTrainerList.stream()
                .map(DTOMapper::mapTrainerModelToTrainerListItemDTO)
                .toList();
        traineeUpdatedProfileDTO.setTrainerList(trainerListItemDTOList);

        return ResponseEntity.ok(traineeUpdatedProfileDTO);
    }

    @DeleteMapping("/{trainee}/delete")
    @Operation(summary = "Delete trainee profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainee profile deleted",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied for this user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity deleteTrainee(@Parameter(description = "Trainee username")
                                         @PathVariable("trainee") String username) throws IncorrectCredentialException {
        service.delete(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{trainee}/not-assigned-trainers")
    @Operation(summary = "Gets unassigned trainers on trainee")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get trainees unassigned trainers list success",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TrainerListDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied for this user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<TrainerListDTO> getNotAssignedTrainersProfile(
            @Parameter(description = "Trainee username")
            @PathVariable("trainee") String username)
            throws IncorrectCredentialException {
        List<TrainerListItemDTO> trainerList = service.getNotAssignedTrainerList(username).stream()
                .filter(TrainerModel::getIsActive)
                .map(DTOMapper::mapTrainerModelToTrainerListItemDTO)
                .toList();
        TrainerListDTO notAssignedActiveTrainerList = TrainerListDTO.builder()
                .trainers(trainerList)
                .build();
        return ResponseEntity.ok(notAssignedActiveTrainerList);
    }

    @PutMapping("/{trainee}/trainers")
    @Operation(summary = "Update trainee's trainer list")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainer list updated",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TrainerListDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied for this user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<TrainerListDTO> updateTraineesTrainerList(
            @RequestBody @Valid TraineeUpdateTrainerListDTO traineeUpdateTrainerListDTO,
            @Parameter(description = "Trainee username")
            @PathVariable("trainee") String username)
            throws IncorrectCredentialException {
        List<TrainerModel> newTrainerModelList = service.updateTrainerList(username,
                traineeUpdateTrainerListDTO.getTrainerList());
        List<TrainerListItemDTO> newTrainerList = newTrainerModelList.stream()
                .map(DTOMapper::mapTrainerModelToTrainerListItemDTO)
                .toList();
        TrainerListDTO trainerListDTO = TrainerListDTO.builder()
                .trainers(newTrainerList)
                .build();
        return ResponseEntity.ok(trainerListDTO);
    }

    @GetMapping("/{trainee}/trainings")
    @Operation(summary = "Gets training list")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Get trainees training list success",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TraineeTrainingListItemDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied for this user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity<List<TraineeTrainingListItemDTO>> getTrainingList(
            @Parameter(description = "Trainee username")
            @PathVariable("trainee") String username,
            @Parameter(description = "End date for training list period")
            @RequestParam(defaultValue = "", name = "periodTo") String periodTo,
            @Parameter(description = "Start date for training list period")
            @RequestParam(defaultValue = "", name = "periodFrom") String periodFrom,
            @Parameter(description = "Trainer username for training list period")
            @RequestParam(defaultValue = "", name = "trainer") String trainer,
            @Parameter(description = "Training type for training list period")
            @RequestParam(defaultValue = "", name = "trainingType") String trainingType
    )
            throws IncorrectCredentialException {
        List<TraineeTrainingListItemDTO> trainingList =
                service.getTrainingList(username, periodFrom, periodTo, trainer, trainingType);
        return ResponseEntity.ok(trainingList);
    }

    @PatchMapping("/{trainee}/activate")
    @Operation(summary = "Activate trainee profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainee profile activated",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied for this user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity activateTrainee(@Parameter(description = "Trainee username")
                                           @PathVariable("trainee") String username)
            throws IncorrectCredentialException {
        service.activate(username);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{trainee}/deactivate")
    @Operation(summary = "Deactivate trainee profile")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trainee profile deactivated",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    }),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied for this user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity deactivateTrainee(@Parameter(description = "Trainee username")
                                             @PathVariable("trainee") String username)
            throws IncorrectCredentialException {
        service.deactivate(username);
        return ResponseEntity.ok().build();
    }
}

