/*
 * Copyright 2025 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.common.config.web.controllers;

import com.firefly.common.config.core.services.ProviderParameterService;
import com.firefly.common.config.interfaces.dtos.ProviderParameterDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/provider-parameters")
@RequiredArgsConstructor
@Tag(name = "Provider Parameters", description = "API for managing provider parameters and configuration")
public class ProviderParameterController {

    private final ProviderParameterService providerParameterService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getProviderParameterById",
            summary = "Get a provider parameter by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProviderParameterDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Provider parameter not found")
            }
    )
    public ResponseEntity<Mono<ProviderParameterDTO>> getById(
            @Parameter(description = "ID of the provider parameter to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(providerParameterService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterProviderParameters",
            summary = "Filter provider parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<ProviderParameterDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<ProviderParameterDTO> filterRequest) {
        return ResponseEntity.ok(providerParameterService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createProviderParameter",
            summary = "Create a new provider parameter",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Provider parameter created", content = @Content(schema = @Schema(implementation = ProviderParameterDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<ProviderParameterDTO>> create(
            @Parameter(description = "Provider parameter to create", required = true)
            @Valid @RequestBody ProviderParameterDTO providerParameterDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(providerParameterService.create(providerParameterDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateProviderParameter",
            summary = "Update an existing provider parameter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Provider parameter updated", content = @Content(schema = @Schema(implementation = ProviderParameterDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Provider parameter not found")
            }
    )
    public ResponseEntity<Mono<ProviderParameterDTO>> update(
            @Parameter(description = "ID of the provider parameter to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Provider parameter to update", required = true)
            @Valid @RequestBody ProviderParameterDTO providerParameterDTO) {
        return ResponseEntity.ok(providerParameterService.update(id, providerParameterDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "deleteProviderParameter",
            summary = "Delete a provider parameter",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Provider parameter deleted"),
                    @ApiResponse(responseCode = "404", description = "Provider parameter not found")
            }
    )
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID of the provider parameter to delete", required = true)
            @PathVariable UUID id) {
        return providerParameterService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}

