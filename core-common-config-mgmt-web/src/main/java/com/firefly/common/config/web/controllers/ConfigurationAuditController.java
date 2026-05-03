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

import com.firefly.common.config.core.services.ConfigurationAuditService;
import com.firefly.common.config.interfaces.dtos.ConfigurationAuditDTO;
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
@RequestMapping("/api/v1/configuration-audits")
@RequiredArgsConstructor
@Tag(name = "Configuration Audits", description = "API for managing configuration audit trail with rollback capabilities")
public class ConfigurationAuditController {

    private final ConfigurationAuditService configurationAuditService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getConfigurationAuditById",
            summary = "Get configuration audit by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ConfigurationAuditDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Configuration audit not found")
            }
    )
    public ResponseEntity<Mono<ConfigurationAuditDTO>> getById(
            @Parameter(description = "ID of the configuration audit to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(configurationAuditService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterConfigurationAudits",
            summary = "Filter configuration audits",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<ConfigurationAuditDTO>>> filter(
            @ParameterObject @ModelAttribute FilterRequest<ConfigurationAuditDTO> filterRequest) {
        return ResponseEntity.ok(configurationAuditService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createConfigurationAudit",
            summary = "Create a new configuration audit entry",
            description = "Audit entries are immutable and cannot be updated or deleted once created",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Configuration audit created", content = @Content(schema = @Schema(implementation = ConfigurationAuditDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<ConfigurationAuditDTO>> create(
            @Parameter(description = "Configuration audit to create", required = true)
            @Valid @RequestBody ConfigurationAuditDTO configurationAuditDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(configurationAuditService.create(configurationAuditDTO));
    }
}

