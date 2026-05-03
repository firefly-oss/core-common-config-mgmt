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
import com.firefly.common.config.core.services.TenantService;
import com.firefly.common.config.interfaces.dtos.ProviderTenantDTO;
import com.firefly.common.config.interfaces.dtos.TenantDTO;
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
 * REST controller for managing Tenants in the Firefly core banking platform.
 *
 * <p>Tenants represent logical separations within Firefly, allowing multiple banking organizations
 * to share infrastructure while maintaining independent configurations, providers, and settings.
 * Each tenant has its own branding, operational settings, and provider relationships.</p>
 *
 * <p>This controller provides comprehensive CRUD operations for tenant management, including
 * filtering, provider relationship management, and nested resource access.</p>
 */
@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Tag(
    name = "Tenant Management",
    description = "Manage logical tenant separations in Firefly core banking. " +
                  "Tenants enable multi-tenancy where different banking organizations share infrastructure " +
                  "while maintaining separate configurations, providers, branding, and operational settings. " +
                  "Use these APIs to create, configure, and manage tenant instances, their provider relationships, " +
                  "and access tenant-specific resources like settings and branding."
)
public class TenantController {

    private final TenantService tenantService;
    private final ProviderTenantService providerTenantService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getTenantById",
            summary = "Retrieve tenant by ID",
            description = "Fetches complete tenant information including legal entity details, hierarchical structure, " +
                         "risk ratings, compliance tier, subscription details, and contact information. " +
                         "Returns 404 if tenant does not exist or is not accessible.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Tenant successfully retrieved",
                        content = @Content(schema = @Schema(implementation = TenantDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Tenant not found - The specified tenant ID does not exist or has been deleted"
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Access denied - Insufficient permissions to view this tenant"
                    )
            }
    )
    public ResponseEntity<Mono<TenantDTO>> getById(
            @Parameter(
                description = "Unique identifier (UUID) of the tenant to retrieve",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        return ResponseEntity.ok(tenantService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterTenants",
            summary = "Search and filter tenants",
            description = "Performs advanced filtering and pagination of tenants based on multiple criteria. " +
                         "Supports filtering by status, type, risk rating, compliance tier, subscription tier, " +
                         "parent tenant, and custom metadata. Results are paginated and can be sorted by any field. " +
                         "Useful for administrative dashboards, reporting, and tenant discovery.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Filtered tenant list successfully retrieved with pagination metadata"
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid filter criteria - Check filter syntax and field names"
                    )
            }
    )
    public ResponseEntity<Mono<PaginationResponse<TenantDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<TenantDTO> filterRequest) {
        return ResponseEntity.ok(tenantService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createTenant",
            summary = "Create a new tenant",
            description = "Creates a new tenant instance in the Firefly platform. This establishes a new logical " +
                         "separation with independent configuration space. Required fields include code (unique identifier), " +
                         "name, and tenant status. Optional fields allow setting legal entity information, hierarchical " +
                         "relationships (parent tenant), risk ratings, compliance requirements, and subscription details. " +
                         "Upon creation, default settings and branding can be initialized automatically.",
            responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Tenant successfully created - Returns the created tenant with generated ID and timestamps",
                        content = @Content(schema = @Schema(implementation = TenantDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input - Validation errors in tenant data (e.g., duplicate code, missing required fields)"
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflict - A tenant with the specified code already exists"
                    )
            }
    )
    public ResponseEntity<Mono<TenantDTO>> create(
            @Parameter(
                description = "Tenant data to create. Must include unique code, name, and tenant status ID. " +
                             "Optionally include legal entity details, parent tenant reference, risk/compliance settings, " +
                             "and subscription information.",
                required = true
            )
            @Valid @RequestBody TenantDTO tenantDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tenantService.create(tenantDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateTenant",
            summary = "Update existing tenant",
            description = "Updates tenant information including legal entity details, contact information, risk ratings, " +
                         "compliance settings, subscription details, and metadata. Uses optimistic locking via version field " +
                         "to prevent concurrent modification conflicts. All fields except ID and audit timestamps can be updated. " +
                         "Changing tenant status or type may trigger validation rules and workflow approvals.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Tenant successfully updated - Returns updated tenant with new version number",
                        content = @Content(schema = @Schema(implementation = TenantDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input - Validation errors or business rule violations"
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Tenant not found - The specified tenant ID does not exist"
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflict - Version mismatch due to concurrent modification"
                    )
            }
    )
    public ResponseEntity<Mono<TenantDTO>> update(
            @Parameter(
                description = "Unique identifier (UUID) of the tenant to update",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,
            @Parameter(
                description = "Updated tenant data. Include version field for optimistic locking. " +
                             "ID in body must match path parameter.",
                required = true
            )
            @Valid @RequestBody TenantDTO tenantDTO) {
        return ResponseEntity.ok(tenantService.update(id, tenantDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "deleteTenant",
            summary = "Delete tenant",
            description = "Permanently deletes a tenant and all associated resources including settings, branding, " +
                         "provider relationships, and audit history. This is a destructive operation that cannot be undone. " +
                         "Consider deactivating the tenant instead by updating its status to INACTIVE. " +
                         "Deletion may fail if tenant has active transactions or dependencies.",
            responses = {
                    @ApiResponse(
                        responseCode = "204",
                        description = "Tenant successfully deleted - No content returned"
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Tenant not found - The specified tenant ID does not exist"
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflict - Cannot delete tenant with active dependencies or transactions"
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - Insufficient permissions to delete tenant"
                    )
            }
    )
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(
                description = "Unique identifier (UUID) of the tenant to delete",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        return tenantService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    // Nested resource: Tenant Providers

    @PostMapping("/{tenantId}/providers/filter")
    @Operation(
            operationId = "filterTenantProviders",
            summary = "List tenant's provider relationships",
            description = "Retrieves all provider relationships configured for a specific tenant with filtering and pagination. " +
                         "Shows which external service providers (payment gateways, KYC services, etc.) are available to this tenant, " +
                         "including configuration overrides, billing settings, priority, failover configuration, and usage metrics. " +
                         "Useful for managing tenant-specific provider integrations and monitoring provider health.",
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Provider relationships successfully retrieved with pagination metadata"
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Tenant not found - The specified tenant ID does not exist"
                    )
            }
    )
    public ResponseEntity<Mono<PaginationResponse<ProviderTenantDTO>>> filterProviders(
            @Parameter(
                description = "Unique identifier (UUID) of the tenant whose providers to retrieve",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID tenantId,
            @RequestBody FilterRequest<ProviderTenantDTO> filterRequest) {

        filterRequest.getFilters().setTenantId(tenantId);
        return ResponseEntity.ok(providerTenantService.filter(filterRequest));
    }

    @PostMapping("/{tenantId}/providers")
    @Operation(
            operationId = "addProviderToTenant",
            summary = "Associate provider with tenant",
            description = "Creates a new provider-tenant relationship, enabling the tenant to use a specific external service provider. " +
                         "Configure provider-specific settings including priority (for load balancing), billing model, transaction limits, " +
                         "failover behavior, circuit breaker settings, and custom configuration overrides. " +
                         "Multiple providers of the same type can be configured with different priorities for redundancy and load distribution.",
            responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Provider successfully associated with tenant - Returns the created relationship configuration",
                        content = @Content(schema = @Schema(implementation = ProviderTenantDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input - Validation errors or invalid provider/tenant combination"
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Tenant or provider not found"
                    ),
                    @ApiResponse(
                        responseCode = "409",
                        description = "Conflict - Provider already associated with this tenant"
                    )
            }
    )
    public ResponseEntity<Mono<ProviderTenantDTO>> addProvider(
            @Parameter(
                description = "Unique identifier (UUID) of the tenant",
                required = true,
                example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID tenantId,
            @Parameter(
                description = "Provider-tenant relationship configuration including provider ID, priority, billing settings, " +
                             "failover configuration, and custom overrides.",
                required = true
            )
            @Valid @RequestBody ProviderTenantDTO providerTenantDTO) {
        providerTenantDTO.setTenantId(tenantId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(providerTenantService.create(providerTenantDTO));
    }

    @GetMapping("/{tenantId}/providers/{id}")
    @Operation(
            operationId = "getTenantProviderById",
            summary = "Get tenant-provider relationship details",
            description = "Retrieves detailed configuration of a specific provider-tenant relationship including " +
                         "configuration overrides, billing settings, usage metrics, health status, and failover configuration.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProviderTenantDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Provider-tenant relationship not found")
            }
    )
    public ResponseEntity<Mono<ProviderTenantDTO>> getProviderById(
            @Parameter(description = "ID of the tenant", required = true)
            @PathVariable UUID tenantId,
            @Parameter(description = "ID of the provider-tenant relationship to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(providerTenantService.getById(id)
                .filter(pt -> pt.getTenantId().equals(tenantId)));
    }

    @PutMapping("/{tenantId}/providers/{id}")
    @Operation(
            operationId = "updateTenantProvider",
            summary = "Update a provider-tenant relationship",
            description = "Updates a provider-tenant relationship for a specific tenant and returns it",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Provider-tenant relationship updated", content = @Content(schema = @Schema(implementation = ProviderTenantDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Provider-tenant relationship not found")
            }
    )
    public ResponseEntity<Mono<ProviderTenantDTO>> updateProvider(
            @Parameter(description = "ID of the tenant", required = true)
            @PathVariable UUID tenantId,
            @Parameter(description = "ID of the provider-tenant relationship to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Provider-tenant relationship to update", required = true)
            @Valid @RequestBody ProviderTenantDTO providerTenantDTO) {
        providerTenantDTO.setTenantId(tenantId);
        return ResponseEntity.ok(providerTenantService.update(id, providerTenantDTO));
    }

    @DeleteMapping("/{tenantId}/providers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "removeTenantProvider",
            summary = "Remove a provider from a tenant",
            description = "Deletes a provider-tenant relationship for a specific tenant",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Provider removed from tenant"),
                    @ApiResponse(responseCode = "404", description = "Provider-tenant relationship not found")
            }
    )
    public Mono<ResponseEntity<Void>> removeProvider(
            @Parameter(description = "ID of the tenant", required = true)
            @PathVariable UUID tenantId,
            @Parameter(description = "ID of the provider-tenant relationship to delete", required = true)
            @PathVariable UUID id) {
        return providerTenantService.getById(id)
                .filter(pt -> pt.getTenantId().equals(tenantId))
                .flatMap(pt -> providerTenantService.delete(id))
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

