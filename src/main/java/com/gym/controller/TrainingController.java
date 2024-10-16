package com.gym.controller;

import com.gym.dto.ResponseErrorBodyDTO;
import com.gym.dto.training.TrainingCreateDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/training")
@RequiredArgsConstructor
@Tag(name = "Training", description = "Operations for creating trainings in the application")
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
}
