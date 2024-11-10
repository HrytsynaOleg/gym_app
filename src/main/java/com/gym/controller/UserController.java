package com.gym.controller;

import com.gym.dto.ChangePasswordDTO;
import com.gym.dto.ResponseErrorBodyDTO;
import com.gym.exception.IncorrectCredentialException;
import com.gym.model.UserCredentials;
import com.gym.service.IUserService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations for login users and changing password")
public class UserController {

    @Autowired
    private final IUserService service;

    @GetMapping("/{user}")
    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login success",
                    content = {
                            @Content(
                                    mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User unauthorized",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ResponseErrorBodyDTO.class)))
                    })
    })
    private ResponseEntity login(@Parameter(description = "Username") @PathVariable("user") String userName,
                                 @Parameter(description = "User password") @RequestHeader("password") String password)
            throws IncorrectCredentialException {
        UserCredentials credentials = UserCredentials.builder()
                .userName(userName)
                .password(password)
                .build();
        service.login(credentials);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{user}")
    @Operation(summary = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login changed successfully",
                    content = {
                            @Content(
                                    mediaType = "")}
            ),
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
    private ResponseEntity changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO)
            throws IncorrectCredentialException {
        service.changePassword(changePasswordDTO.getUserName(), changePasswordDTO.getNewPassword());
        return ResponseEntity.ok().build();
    }

}
