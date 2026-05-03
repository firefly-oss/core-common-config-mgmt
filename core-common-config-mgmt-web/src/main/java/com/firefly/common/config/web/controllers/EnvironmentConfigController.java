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

import com.firefly.common.config.core.services.EnvironmentConfigService;
import com.firefly.common.config.interfaces.dtos.EnvironmentConfigDTO;
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
@RequestMapping("/api/v1/environment-configs")
@RequiredArgsConstructor
@Tag(name = "Environment Configurations", description = "API for managing environment-specific configurations (dev, staging, production)")
public class EnvironmentConfigController {

    private final EnvironmentConfigService environmentConfigService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getEnvironmentConfigById",
            summary = "Get environment configuration by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = EnvironmentConfigDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Environment configuration not found")
            }
    )
    public ResponseEntity<Mono<EnvironmentConfigDTO>> getById(
            @Parameter(description = "ID of the environment configuration to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(environmentConfigService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterEnvironmentConfigs",
            summary = "Filter environment configurations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<EnvironmentConfigDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<EnvironmentConfigDTO> filterRequest) {
        return ResponseEntity.ok(environmentConfigService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createEnvironmentConfig",
            summary = "Create a new environment configuration",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Environment configuration created", content = @Content(schema = @Schema(implementation = EnvironmentConfigDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<EnvironmentConfigDTO>> create(
            @Parameter(description = "Environment configuration to create", required = true)
            @Valid @RequestBody EnvironmentConfigDTO environmentConfigDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(environmentConfigService.create(environmentConfigDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateEnvironmentConfig",
            summary = "Update an existing environment configuration",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Environment configuration updated", content = @Content(schema = @Schema(implementation = EnvironmentConfigDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Environment configuration not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<EnvironmentConfigDTO>> update(
            @Parameter(description = "ID of the environment configuration to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated environment configuration data", required = true)
            @Valid @RequestBody EnvironmentConfigDTO environmentConfigDTO) {
        return ResponseEntity.ok(environmentConfigService.update(id, environmentConfigDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            operationId = "deleteEnvironmentConfig",
            summary = "Delete an environment configuration",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Environment configuration deleted"),
                    @ApiResponse(responseCode = "404", description = "Environment configuration not found")
            }
    )
    public ResponseEntity<Mono<Void>> delete(
            @Parameter(description = "ID of the environment configuration to delete", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(environmentConfigService.delete(id));
    }
}

