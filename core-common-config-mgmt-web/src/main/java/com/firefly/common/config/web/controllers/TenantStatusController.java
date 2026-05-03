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

import com.firefly.common.config.core.services.TenantStatusService;
import com.firefly.common.config.interfaces.dtos.TenantStatusDTO;
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
 * REST controller for managing tenant statuses
 */
@RestController
@RequestMapping("/api/v1/tenant-statuses")
@RequiredArgsConstructor
@Tag(name = "Tenant Statuses", description = "API for managing tenant statuses")
public class TenantStatusController {

    private final TenantStatusService tenantStatusService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getTenantStatusById",
            summary = "Get a tenant status by ID",
            description = "Returns a tenant status based on the ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = TenantStatusDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tenant status not found")
            }
    )
    public ResponseEntity<Mono<TenantStatusDTO>> getById(
            @Parameter(description = "ID of the tenant status to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(tenantStatusService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterTenantStatuses",
            summary = "Filter tenant statuses",
            description = "Returns a filtered list of tenant statuses based on criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<TenantStatusDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<TenantStatusDTO> filterRequest) {
        return ResponseEntity.ok(tenantStatusService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createTenantStatus",
            summary = "Create a new tenant status",
            description = "Creates a new tenant status and returns it",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tenant status created", content = @Content(schema = @Schema(implementation = TenantStatusDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<TenantStatusDTO>> create(
            @Parameter(description = "Tenant status to create", required = true)
            @Valid @RequestBody TenantStatusDTO tenantStatusDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tenantStatusService.create(tenantStatusDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateTenantStatus",
            summary = "Update an existing tenant status",
            description = "Updates an existing tenant status and returns it",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tenant status updated", content = @Content(schema = @Schema(implementation = TenantStatusDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Tenant status not found")
            }
    )
    public ResponseEntity<Mono<TenantStatusDTO>> update(
            @Parameter(description = "ID of the tenant status to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Tenant status to update", required = true)
            @Valid @RequestBody TenantStatusDTO tenantStatusDTO) {
        return ResponseEntity.ok(tenantStatusService.update(id, tenantStatusDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "deleteTenantStatus",
            summary = "Delete a tenant status",
            description = "Deletes a tenant status",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tenant status deleted"),
                    @ApiResponse(responseCode = "404", description = "Tenant status not found")
            }
    )
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID of the tenant status to delete", required = true)
            @PathVariable UUID id) {
        return tenantStatusService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}

