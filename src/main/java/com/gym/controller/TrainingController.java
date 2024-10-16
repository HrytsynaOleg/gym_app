package com.gym.controller;

import com.gym.dto.ResponseErrorBodyDTO;
import com.gym.dto.training.TrainingCreateDTO;
import com.gym.dto.training.TrainingTypeListDTO;
import com.gym.dto.training.TrainingTypeListItemDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.TrainingTypeEnum;
import com.gym.service.ITrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/training")
@RequiredArgsConstructor
@Tag(name = "Training", description = "Operations for creating trainings and retrieving training types in the application")
public class TrainingController {

    @Autowired
    private final ITrainingService service;

    @PostMapping
    @Operation(summary = "Create new training")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "New training successfully created",
                    content = {
                            @Content(mediaType = "application/json")
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
    private ResponseEntity createTraining(@RequestBody @Valid TrainingCreateDTO trainingCreateDTO) {
        service.createTraining(trainingCreateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/types")
    @Operation(summary = "Gets training types list")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Gets training types list success",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TrainingTypeListDTO.class)))
                    })
    })
    private ResponseEntity<TrainingTypeListDTO> getTrainingList()
            throws IncorrectCredentialException {
        List<TrainingTypeListItemDTO> trainingTypeList = Arrays.stream(TrainingTypeEnum.values())
                .map(e ->
            TrainingTypeListItemDTO.builder()
                    .trainingTypeName(e.getName())
                    .trainingTypeId(e.getId())
                    .build())
                .toList();
        TrainingTypeListDTO trainingTypeListDTO = TrainingTypeListDTO.builder()
                .trainingTypes(trainingTypeList)
                .build();
        return ResponseEntity.ok(trainingTypeListDTO);
    }
}
