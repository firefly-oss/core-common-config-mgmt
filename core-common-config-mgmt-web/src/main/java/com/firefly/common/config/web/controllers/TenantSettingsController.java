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

import com.firefly.common.config.core.services.TenantSettingsService;
import com.firefly.common.config.interfaces.dtos.TenantSettingsDTO;
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
@RequestMapping("/api/v1/tenant-settings")
@RequiredArgsConstructor
@Tag(name = "Tenant Settings", description = "API for managing tenant operational settings including rate limiting, security policies, circuit breakers, and compliance")
public class TenantSettingsController {

    private final TenantSettingsService tenantSettingsService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getTenantSettingsById",
            summary = "Get tenant settings by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = TenantSettingsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tenant settings not found")
            }
    )
    public ResponseEntity<Mono<TenantSettingsDTO>> getById(
            @Parameter(description = "ID of the tenant settings to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(tenantSettingsService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterTenantSettings",
            summary = "Filter tenant settings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<TenantSettingsDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<TenantSettingsDTO> filterRequest) {
        return ResponseEntity.ok(tenantSettingsService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createTenantSettings",
            summary = "Create new tenant settings",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tenant settings created", content = @Content(schema = @Schema(implementation = TenantSettingsDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<TenantSettingsDTO>> create(
            @Parameter(description = "Tenant settings to create", required = true)
            @Valid @RequestBody TenantSettingsDTO tenantSettingsDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantSettingsService.create(tenantSettingsDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateTenantSettings",
            summary = "Update existing tenant settings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tenant settings updated", content = @Content(schema = @Schema(implementation = TenantSettingsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Tenant settings not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<TenantSettingsDTO>> update(
            @Parameter(description = "ID of the tenant settings to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated tenant settings data", required = true)
            @Valid @RequestBody TenantSettingsDTO tenantSettingsDTO) {
        return ResponseEntity.ok(tenantSettingsService.update(id, tenantSettingsDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            operationId = "deleteTenantSettings",
            summary = "Delete tenant settings",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tenant settings deleted"),
                    @ApiResponse(responseCode = "404", description = "Tenant settings not found")
            }
    )
    public ResponseEntity<Mono<Void>> delete(
            @Parameter(description = "ID of the tenant settings to delete", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(tenantSettingsService.delete(id));
    }
}

