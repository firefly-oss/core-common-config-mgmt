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

import com.firefly.common.config.core.services.ProviderService;
import com.firefly.common.config.interfaces.dtos.ProviderDTO;
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

/**
 * REST controller for managing External Service Providers in the Firefly core banking platform.
 *
 * <p>Providers represent external services that Firefly integrates with to deliver banking functionality.
 * This includes payment gateways (Stripe, PayPal), KYC/AML providers (Onfido, Jumio), card issuers,
 * fraud detection services, and other third-party integrations.</p>
 *
 * <p>This controller provides comprehensive CRUD operations for provider management, including
 * configuration, health monitoring, SLA tracking, and contract management.</p>
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
@Tag(
    name = "Provider Management",
    description = "Manage external service provider integrations for Firefly core banking. " +
                  "Providers are third-party services that Firefly integrates with to deliver banking capabilities " +
                  "such as payment processing, KYC/AML verification, card issuing, fraud detection, and more. " +
                  "Use these APIs to register providers, configure API endpoints, manage credentials, track SLAs, " +
                  "monitor health status, and maintain provider contracts and certifications."
)
public class ProviderController {

    private final ProviderService providerService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getProviderById",
            summary = "Retrieve provider by ID",
            description = "Fetches complete provider information including API configuration (production/sandbox URLs), " +
                         "supported capabilities, SLA commitments, compliance certifications, contract details, " +
                         "contact information, and current health status. Returns 404 if provider does not exist.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Provider successfully retrieved",
                        content = @Content(schema = @Schema(implementation = ProviderDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Provider not found - The specified provider ID does not exist or has been deleted"
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Access denied - Insufficient permissions to view this provider"
                    )
            }
    )
    public ResponseEntity<Mono<ProviderDTO>> getById(
            @Parameter(
                description = "Unique identifier (UUID) of the provider to retrieve",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        return ResponseEntity.ok(providerService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterProviders",
            summary = "Search and filter providers",
            description = "Performs advanced filtering and pagination of providers based on multiple criteria. " +
                         "Supports filtering by provider type (PAYMENT_GATEWAY, KYC, CARD_ISSUING, etc.), status, " +
                         "supported countries/currencies, certification level, health status, and custom metadata. " +
                         "Results are paginated and can be sorted by any field. Useful for provider discovery, " +
                         "administrative dashboards, and integration planning.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Filtered provider list successfully retrieved with pagination metadata"
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid filter criteria - Check filter syntax and field names"
                    )
            }
    )
    public ResponseEntity<Mono<PaginationResponse<ProviderDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<ProviderDTO> filterRequest) {
        return ResponseEntity.ok(providerService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createProvider",
            summary = "Register a new provider",
            description = "Registers a new external service provider in the Firefly platform. Required fields include unique code, " +
                         "name, provider type (PAYMENT_GATEWAY, KYC, CARD_ISSUING, etc.), and status. Configure API endpoints " +
                         "(production/sandbox URLs), supported capabilities (countries, currencies, languages), SLA commitments, " +
                         "compliance certifications, contract details, and contact information. Health monitoring can be enabled " +
                         "by providing a health check URL.",
            responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Provider successfully registered - Returns the created provider with generated ID and timestamps",
                        content = @Content(schema = @Schema(implementation = ProviderDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input - Validation errors in provider data (e.g., duplicate code, invalid URLs)"
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflict - A provider with the specified code already exists"
                    )
            }
    )
    public ResponseEntity<Mono<ProviderDTO>> create(
            @Parameter(
                description = "Provider data to register. Must include unique code, name, provider type ID, and status ID. " +
                             "Optionally include API URLs, capabilities, SLA settings, certifications, and contract details.",
                required = true
            )
            @Valid @RequestBody ProviderDTO providerDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(providerService.create(providerDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateProvider",
            summary = "Update existing provider",
            description = "Updates provider configuration including API endpoints, capabilities, SLA commitments, certifications, " +
                         "contract details, contact information, and health monitoring settings. Uses optimistic locking via version field " +
                         "to prevent concurrent modification conflicts. Changing provider status may affect all tenant relationships " +
                         "using this provider. Health status and metrics are typically updated automatically by monitoring systems.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Provider successfully updated - Returns updated provider with new version number",
                        content = @Content(schema = @Schema(implementation = ProviderDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input - Validation errors or business rule violations"
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Provider not found - The specified provider ID does not exist"
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflict - Version mismatch due to concurrent modification"
                    )
            }
    )
    public ResponseEntity<Mono<ProviderDTO>> update(
            @Parameter(
                description = "Unique identifier (UUID) of the provider to update",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,
            @Parameter(
                description = "Updated provider data. Include version field for optimistic locking. " +
                             "ID in body must match path parameter.",
                required = true
            )
            @Valid @RequestBody ProviderDTO providerDTO) {
        return ResponseEntity.ok(providerService.update(id, providerDTO));
    }

    /**
     * DELETE /api/v1/providers/:id : Delete a provider
     *
     * @param id the ID of the provider to delete
     * @return the ResponseEntity with status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "deleteProvider",
            summary = "Delete a provider",
            description = "Deletes a provider",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Provider deleted"),
                    @ApiResponse(responseCode = "404", description = "Provider not found")
            }
    )
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID of the provider to delete", required = true)
            @PathVariable UUID id) {
        return providerService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}
