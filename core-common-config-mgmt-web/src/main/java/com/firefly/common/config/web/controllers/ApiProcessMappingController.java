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

import com.firefly.common.config.core.services.ApiProcessMappingService;
import com.firefly.common.config.interfaces.dtos.ApiProcessMappingDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.UUID;

/**
 * REST controller for managing API-to-Process mappings.
 * 
 * <p>This controller provides endpoints for the Firefly Plugin Architecture's
 * process mapping configuration, enabling dynamic routing of API operations
 * to specific process implementations.</p>
 * 
 * @author Firefly Development Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/api-process-mappings")
@RequiredArgsConstructor
@Tag(name = "API Process Mappings", description = "API for managing API-to-Process mappings in the Firefly Plugin Architecture")
public class ApiProcessMappingController {

    private final ApiProcessMappingService apiProcessMappingService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getApiProcessMappingById",
            summary = "Get an API process mapping by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", 
                            content = @Content(schema = @Schema(implementation = ApiProcessMappingDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Mapping not found")
            }
    )
    public ResponseEntity<Mono<ApiProcessMappingDTO>> getById(
            @Parameter(description = "ID of the mapping to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(apiProcessMappingService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterApiProcessMappings",
            summary = "Filter API process mappings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<ApiProcessMappingDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<ApiProcessMappingDTO> filterRequest) {
        return ResponseEntity.ok(apiProcessMappingService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createApiProcessMapping",
            summary = "Create a new API process mapping",
            description = "Creates a mapping between an API operation and a process plugin. " +
                    "The mapping can be tenant-specific or vanilla (default for all tenants).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Mapping created",
                            content = @Content(schema = @Schema(implementation = ApiProcessMappingDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<ApiProcessMappingDTO>> create(
            @Parameter(description = "Mapping to create", required = true)
            @Valid @RequestBody ApiProcessMappingDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiProcessMappingService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateApiProcessMapping",
            summary = "Update an existing API process mapping",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mapping updated",
                            content = @Content(schema = @Schema(implementation = ApiProcessMappingDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Mapping not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<ApiProcessMappingDTO>> update(
            @Parameter(description = "ID of the mapping to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated mapping data", required = true)
            @Valid @RequestBody ApiProcessMappingDTO dto) {
        return ResponseEntity.ok(apiProcessMappingService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            operationId = "deleteApiProcessMapping",
            summary = "Delete an API process mapping",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Mapping deleted"),
                    @ApiResponse(responseCode = "404", description = "Mapping not found")
            }
    )
    public ResponseEntity<Mono<Void>> delete(
            @Parameter(description = "ID of the mapping to delete", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiProcessMappingService.delete(id));
    }

    // ========== Plugin Architecture Specific Endpoints ==========

    @GetMapping("/resolve")
    @Operation(
            operationId = "resolveApiProcessMapping",
            summary = "Resolve the process mapping for an API operation",
            description = "Resolves the best matching process mapping based on tenant, operation, product, and channel. " +
                    "Priority resolution order: tenant+product+channel > tenant+product > tenant > vanilla",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mapping resolved",
                            content = @Content(schema = @Schema(implementation = ApiProcessMappingDTO.class))),
                    @ApiResponse(responseCode = "404", description = "No matching mapping found")
            }
    )
    public ResponseEntity<Mono<ApiProcessMappingDTO>> resolve(
            @Parameter(description = "Tenant ID for the resolution context")
            @RequestParam(required = false) UUID tenantId,
            @Parameter(description = "API operation ID (e.g., 'createAccount', 'processPayment')", required = true)
            @RequestParam String operationId,
            @Parameter(description = "Product ID for more specific routing")
            @RequestParam(required = false) UUID productId,
            @Parameter(description = "Channel type (e.g., 'API', 'MOBILE', 'BRANCH')")
            @RequestParam(required = false) String channelType) {
        return ResponseEntity.ok(apiProcessMappingService.resolveMapping(tenantId, operationId, productId, channelType));
    }

    @GetMapping("/tenants/{tenantId}/mappings")
    @Operation(
            operationId = "getApiProcessMappingsByTenant",
            summary = "Get all API process mappings for a tenant",
            description = "Retrieves all custom process mappings configured for a specific tenant.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ApiProcessMappingDTO.class))))
            }
    )
    public ResponseEntity<Flux<ApiProcessMappingDTO>> getByTenantId(
            @Parameter(description = "Tenant ID", required = true)
            @PathVariable UUID tenantId) {
        return ResponseEntity.ok(apiProcessMappingService.getByTenantId(tenantId));
    }

    @GetMapping("/vanilla")
    @Operation(
            operationId = "getVanillaApiProcessMappings",
            summary = "Get all vanilla (default) API process mappings",
            description = "Retrieves all vanilla mappings that serve as defaults when no tenant-specific mapping exists.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ApiProcessMappingDTO.class))))
            }
    )
    public ResponseEntity<Flux<ApiProcessMappingDTO>> getVanillaMappings() {
        return ResponseEntity.ok(apiProcessMappingService.getVanillaMappings());
    }

    @GetMapping("/processes/{processId}")
    @Operation(
            operationId = "getApiProcessMappingsByProcess",
            summary = "Get all API mappings for a process",
            description = "Retrieves all mappings that route to a specific process plugin.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ApiProcessMappingDTO.class))))
            }
    )
    public ResponseEntity<Flux<ApiProcessMappingDTO>> getByProcessId(
            @Parameter(description = "Process ID (plugin identifier)", required = true)
            @PathVariable String processId) {
        return ResponseEntity.ok(apiProcessMappingService.getByProcessId(processId));
    }

    @PostMapping("/cache/invalidate")
    @Operation(
            operationId = "invalidateApiProcessMappingCache",
            summary = "Invalidate the API process mapping cache",
            description = "Forces cache invalidation for process mappings. If tenantId is provided, " +
                    "only that tenant's cache is invalidated; otherwise, the entire cache is cleared.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cache invalidated")
            }
    )
    public ResponseEntity<Mono<Void>> invalidateCache(
            @Parameter(description = "Tenant ID for targeted invalidation (optional)")
            @RequestParam(required = false) UUID tenantId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiProcessMappingService.invalidateCache(tenantId));
    }
}
