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

import com.firefly.common.config.core.services.ProviderTenantService;
import com.firefly.common.config.interfaces.dtos.ProviderTenantDTO;
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
 * REST controller for managing Provider-Tenant Relationships in the Firefly core banking platform.
 *
 * <p>Provider-Tenant relationships define how tenants use external service providers. Each relationship
 * includes configuration overrides, billing settings, priority for load balancing, failover configuration,
 * circuit breaker settings, and usage metrics. This enables fine-grained control over provider integrations
 * at the tenant level.</p>
 */
@RestController
@RequestMapping("/api/v1/provider-tenants")
@RequiredArgsConstructor
@Tag(
    name = "Provider-Tenant Relationships",
    description = "Manage provider integration configurations for specific tenants. " +
                  "Provider-Tenant relationships define how a tenant uses an external service provider, including " +
                  "tenant-specific configuration overrides, billing models (transaction-based, volume-based, flat-fee), " +
                  "priority for load balancing, failover settings, circuit breaker thresholds, transaction limits, " +
                  "and usage tracking. Use these APIs to associate providers with tenants, configure relationship settings, " +
                  "monitor usage metrics, and manage provider failover strategies."
)
public class ProviderTenantController {

    private final ProviderTenantService providerTenantService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getProviderTenantById",
            summary = "Retrieve provider-tenant relationship by ID",
            description = "Fetches complete provider-tenant relationship configuration including priority, billing settings " +
                         "(cost per transaction, monthly fees, transaction limits), configuration overrides, failover settings, " +
                         "circuit breaker configuration, approval details, and usage metrics (total requests, failures, average response time).",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Provider-tenant relationship successfully retrieved",
                        content = @Content(schema = @Schema(implementation = ProviderTenantDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Provider-tenant relationship not found - The specified relationship ID does not exist"
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Access denied - Insufficient permissions to view this relationship"
                    )
            }
    )
    public ResponseEntity<Mono<ProviderTenantDTO>> getById(
            @Parameter(
                description = "Unique identifier (UUID) of the provider-tenant relationship to retrieve",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        return ResponseEntity.ok(providerTenantService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterProviderTenants",
            summary = "Search and filter provider-tenant relationships",
            description = "Performs advanced filtering and pagination of provider-tenant relationships. Supports filtering by " +
                         "tenant, provider, relationship type (STANDARD, PREMIUM, TRIAL), enabled status, billing model, " +
                         "and date ranges. Results include usage metrics for monitoring provider performance per tenant. " +
                         "Useful for tenant management dashboards, billing reports, and provider usage analysis.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Filtered provider-tenant relationships successfully retrieved with pagination metadata"
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid filter criteria - Check filter syntax and field names"
                    )
            }
    )
    public ResponseEntity<Mono<PaginationResponse<ProviderTenantDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<ProviderTenantDTO> filterRequest) {
        return ResponseEntity.ok(providerTenantService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createProviderTenant",
            summary = "Create a new provider-tenant relationship",
            description = "Creates a new provider-tenant relationship and returns it",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Provider-tenant relationship created", content = @Content(schema = @Schema(implementation = ProviderTenantDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<ProviderTenantDTO>> create(
            @Parameter(description = "Provider-tenant relationship to create", required = true)
            @Valid @RequestBody ProviderTenantDTO providerTenantDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(providerTenantService.create(providerTenantDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateProviderTenant",
            summary = "Update an existing provider-tenant relationship",
            description = "Updates an existing provider-tenant relationship and returns it",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Provider-tenant relationship updated", content = @Content(schema = @Schema(implementation = ProviderTenantDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Provider-tenant relationship not found")
            }
    )
    public ResponseEntity<Mono<ProviderTenantDTO>> update(
            @Parameter(description = "ID of the provider-tenant relationship to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Provider-tenant relationship to update", required = true)
            @Valid @RequestBody ProviderTenantDTO providerTenantDTO) {
        return ResponseEntity.ok(providerTenantService.update(id, providerTenantDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "deleteProviderTenant",
            summary = "Delete a provider-tenant relationship",
            description = "Deletes a provider-tenant relationship",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Provider-tenant relationship deleted"),
                    @ApiResponse(responseCode = "404", description = "Provider-tenant relationship not found")
            }
    )
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID of the provider-tenant relationship to delete", required = true)
            @PathVariable UUID id) {
        return providerTenantService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}

