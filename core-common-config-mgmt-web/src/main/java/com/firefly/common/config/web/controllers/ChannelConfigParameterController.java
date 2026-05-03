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

import com.firefly.common.config.core.services.ChannelConfigParameterService;
import com.firefly.common.config.interfaces.dtos.ChannelConfigParameterDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST controller for ChannelConfigParameter operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/channel-config-parameters")
@RequiredArgsConstructor
@Tag(name = "Channel Config Parameters", description = "Dynamic configuration parameters for channels")
public class ChannelConfigParameterController {

    private final ChannelConfigParameterService service;

    @GetMapping("/{id}")
    @Operation(summary = "Get channel config parameter by ID", description = "Retrieves a channel config parameter by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channel config parameter found",
                    content = @Content(schema = @Schema(implementation = ChannelConfigParameterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Channel config parameter not found")
    })
    public Mono<ChannelConfigParameterDTO> getById(@PathVariable UUID id) {
        log.debug("REST request to get ChannelConfigParameter by ID: {}", id);
        return service.getById(id);
    }

    @PostMapping("/filter")
    @Operation(summary = "Filter channel config parameters", description = "Filters channel config parameters with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channel config parameters filtered successfully",
                    content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    })
    public Mono<PaginationResponse<ChannelConfigParameterDTO>> filter(
            @Valid @RequestBody FilterRequest<ChannelConfigParameterDTO> filterRequest) {
        log.debug("REST request to filter ChannelConfigParameters: {}", filterRequest);
        return service.filter(filterRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create channel config parameter", description = "Creates a new channel config parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Channel config parameter created successfully",
                    content = @Content(schema = @Schema(implementation = ChannelConfigParameterDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<ChannelConfigParameterDTO> create(@Valid @RequestBody ChannelConfigParameterDTO dto) {
        log.debug("REST request to create ChannelConfigParameter: {}", dto);
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update channel config parameter", description = "Updates an existing channel config parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Channel config parameter updated successfully",
                    content = @Content(schema = @Schema(implementation = ChannelConfigParameterDTO.class))),
            @ApiResponse(responseCode = "404", description = "Channel config parameter not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<ChannelConfigParameterDTO> update(@PathVariable UUID id, @Valid @RequestBody ChannelConfigParameterDTO dto) {
        log.debug("REST request to update ChannelConfigParameter with ID: {}", id);
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete channel config parameter", description = "Deletes a channel config parameter (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Channel config parameter deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Channel config parameter not found")
    })
    public Mono<Void> delete(@PathVariable UUID id) {
        log.debug("REST request to delete ChannelConfigParameter with ID: {}", id);
        return service.delete(id);
    }
}

