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

import com.firefly.common.config.core.services.ChannelConfigService;
import com.firefly.common.config.interfaces.dtos.ChannelConfigDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller for managing channel configurations
 */
@RestController
@RequestMapping("/api/v1/channel-configs")
@Tag(name = "Channel Configuration", description = "APIs for managing channel configurations (Web Banking, Mobile, ATM, etc.)")
public class ChannelConfigController {

    @Autowired
    private ChannelConfigService channelConfigService;

    @GetMapping("/{id}")
    @Operation(
            operationId = "getChannelConfigById",
            summary = "Get channel config by ID",
            description = "Retrieve a specific channel configuration by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Channel config found", content = @Content(schema = @Schema(implementation = ChannelConfigDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Channel config not found")
            }
    )
    public ResponseEntity<Mono<ChannelConfigDTO>> getById(
            @Parameter(description = "Channel config ID", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(channelConfigService.getById(id));
    }

    @PostMapping("/filter")
    @Operation(
            operationId = "filterChannelConfigs",
            summary = "Filter channel configs",
            description = "Filter channel configurations based on criteria with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Channel configs filtered successfully", content = @Content(schema = @Schema(implementation = PaginationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid filter request")
            }
    )
    public ResponseEntity<Mono<PaginationResponse<ChannelConfigDTO>>> filter(
            @Parameter(description = "Filter request with criteria and pagination", required = true)
            @Valid @RequestBody FilterRequest<ChannelConfigDTO> filterRequest) {
        return ResponseEntity.ok(channelConfigService.filter(filterRequest));
    }

    @PostMapping
    @Operation(
            operationId = "createChannelConfig",
            summary = "Create a new channel config",
            description = "Create a new channel configuration for a tenant",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Channel config created", content = @Content(schema = @Schema(implementation = ChannelConfigDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<ChannelConfigDTO>> create(
            @Parameter(description = "Channel config to create", required = true)
            @Valid @RequestBody ChannelConfigDTO channelConfigDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelConfigService.create(channelConfigDTO));
    }

    @PutMapping("/{id}")
    @Operation(
            operationId = "updateChannelConfig",
            summary = "Update a channel config",
            description = "Update an existing channel configuration",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Channel config updated", content = @Content(schema = @Schema(implementation = ChannelConfigDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Channel config not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<Mono<ChannelConfigDTO>> update(
            @Parameter(description = "Channel config ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated channel config data", required = true)
            @Valid @RequestBody ChannelConfigDTO channelConfigDTO) {
        return ResponseEntity.ok(channelConfigService.update(id, channelConfigDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            operationId = "deleteChannelConfig",
            summary = "Delete a channel config",
            description = "Soft delete a channel configuration (sets active=false)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Channel config deleted"),
                    @ApiResponse(responseCode = "404", description = "Channel config not found")
            }
    )
    public ResponseEntity<Mono<Void>> delete(
            @Parameter(description = "Channel config ID", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.noContent().build();
    }
}

