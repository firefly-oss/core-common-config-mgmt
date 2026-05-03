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

import com.firefly.common.config.core.services.FeatureFlagService;
import com.firefly.common.config.interfaces.dtos.FeatureFlagDTO;
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
@RequestMapping("/api/v1/feature-flags")
@RequiredArgsConstructor
@Tag(name = "Feature Flags", description = "API for managing feature flags and feature toggles")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getFeatureFlagById",
            summary = "Get a feature flag by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = FeatureFlagDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Feature flag not found")
            }
    )
    public ResponseEntity<Mono<FeatureFlagDTO>> getById(
            @Parameter(description = "ID of the feature flag to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(featureFlagService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterFeatureFlags",
            summary = "Filter feature flags",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<FeatureFlagDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<FeatureFlagDTO> filterRequest) {
        return ResponseEntity.ok(featureFlagService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createFeatureFlag",
            summary = "Create a new feature flag",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Feature flag created", content = @Content(schema = @Schema(implementation = FeatureFlagDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<FeatureFlagDTO>> create(
            @Parameter(description = "Feature flag to create", required = true)
            @Valid @RequestBody FeatureFlagDTO featureFlagDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(featureFlagService.create(featureFlagDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateFeatureFlag",
            summary = "Update an existing feature flag",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Feature flag updated", content = @Content(schema = @Schema(implementation = FeatureFlagDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Feature flag not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<FeatureFlagDTO>> update(
            @Parameter(description = "ID of the feature flag to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated feature flag data", required = true)
            @Valid @RequestBody FeatureFlagDTO featureFlagDTO) {
        return ResponseEntity.ok(featureFlagService.update(id, featureFlagDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            operationId = "deleteFeatureFlag",
            summary = "Delete a feature flag",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Feature flag deleted"),
                    @ApiResponse(responseCode = "404", description = "Feature flag not found")
            }
    )
    public ResponseEntity<Mono<Void>> delete(
            @Parameter(description = "ID of the feature flag to delete", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(featureFlagService.delete(id));
    }
}

