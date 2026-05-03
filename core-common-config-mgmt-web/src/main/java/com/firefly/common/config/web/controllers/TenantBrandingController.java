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

import com.firefly.common.config.core.services.TenantBrandingService;
import com.firefly.common.config.interfaces.dtos.TenantBrandingDTO;
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
@RequestMapping("/api/v1/tenant-brandings")
@RequiredArgsConstructor
@Tag(name = "Tenant Brandings", description = "API for managing tenant branding and visual configuration")
public class TenantBrandingController {

    private final TenantBrandingService tenantBrandingService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getTenantBrandingById",
            summary = "Get a tenant branding by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = TenantBrandingDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tenant branding not found")
            }
    )
    public ResponseEntity<Mono<TenantBrandingDTO>> getById(
            @Parameter(description = "ID of the tenant branding to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(tenantBrandingService.getById(id));
    }

    @GetMapping("/tenant/{tenantId}")
    @Operation(
            operationId = "getTenantBrandingByTenantId",
            summary = "Get tenant branding by tenant ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = TenantBrandingDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tenant branding not found")
            }
    )
    public ResponseEntity<Mono<TenantBrandingDTO>> getByTenantId(
            @Parameter(description = "ID of the tenant", required = true)
            @PathVariable UUID tenantId) {
        return ResponseEntity.ok(tenantBrandingService.getByTenantId(tenantId));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterTenantBrandings",
            summary = "Filter tenant brandings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<TenantBrandingDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<TenantBrandingDTO> filterRequest) {
        return ResponseEntity.ok(tenantBrandingService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createTenantBranding",
            summary = "Create a new tenant branding",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tenant branding created", content = @Content(schema = @Schema(implementation = TenantBrandingDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<TenantBrandingDTO>> create(
            @Parameter(description = "Tenant branding to create", required = true)
            @Valid @RequestBody TenantBrandingDTO tenantBrandingDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tenantBrandingService.create(tenantBrandingDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateTenantBranding",
            summary = "Update an existing tenant branding",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tenant branding updated", content = @Content(schema = @Schema(implementation = TenantBrandingDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Tenant branding not found")
            }
    )
    public ResponseEntity<Mono<TenantBrandingDTO>> update(
            @Parameter(description = "ID of the tenant branding to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Tenant branding to update", required = true)
            @Valid @RequestBody TenantBrandingDTO tenantBrandingDTO) {
        return ResponseEntity.ok(tenantBrandingService.update(id, tenantBrandingDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "deleteTenantBranding",
            summary = "Delete a tenant branding",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tenant branding deleted"),
                    @ApiResponse(responseCode = "404", description = "Tenant branding not found")
            }
    )
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "ID of the tenant branding to delete", required = true)
            @PathVariable UUID id) {
        return tenantBrandingService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }
}

