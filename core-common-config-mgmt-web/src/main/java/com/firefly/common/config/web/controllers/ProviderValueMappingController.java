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

import com.firefly.common.config.core.services.ProviderValueMappingService;
import com.firefly.common.config.interfaces.dtos.ProviderValueMappingDTO;
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
@RequestMapping("/api/v1/provider-value-mappings")
@RequiredArgsConstructor
@Tag(name = "Provider Value Mappings", description = "API for managing value mappings between Firefly and providers")
public class ProviderValueMappingController {

    private final ProviderValueMappingService providerValueMappingService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getProviderValueMappingById",
            summary = "Get a provider value mapping by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProviderValueMappingDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Provider value mapping not found")
            }
    )
    public ResponseEntity<Mono<ProviderValueMappingDTO>> getById(
            @Parameter(description = "ID of the provider value mapping to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(providerValueMappingService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterProviderValueMappings",
            summary = "Filter provider value mappings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<ProviderValueMappingDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<ProviderValueMappingDTO> filterRequest) {
        return ResponseEntity.ok(providerValueMappingService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createProviderValueMapping",
            summary = "Create a new provider value mapping",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Provider value mapping created", content = @Content(schema = @Schema(implementation = ProviderValueMappingDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<ProviderValueMappingDTO>> create(
            @Parameter(description = "Provider value mapping to create", required = true)
            @Valid @RequestBody ProviderValueMappingDTO providerValueMappingDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(providerValueMappingService.create(providerValueMappingDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateProviderValueMapping",
            summary = "Update an existing provider value mapping",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Provider value mapping updated", content = @Content(schema = @Schema(implementation = ProviderValueMappingDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Provider value mapping not found")
            }
    )
    public ResponseEntity<Mono<ProviderValueMappingDTO>> update(
            @Parameter(description = "ID of the provider value mapping to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Provider value mapping to update", required = true)
            @Valid @RequestBody ProviderValueMappingDTO providerValueMappingDTO) {
        return ResponseEntity.ok(providerValueMappingService.update(id, providerValueMappingDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "deleteProviderValueMapping",
            summary = "Delete a provider value mapping",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Provider value mapping deleted"),
                    @ApiResponse(responseCode = "404", description = "Provider value mapping not found")
            }
    )
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID of the provider value mapping to delete", required = true)
            @PathVariable UUID id) {
        return providerValueMappingService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}

