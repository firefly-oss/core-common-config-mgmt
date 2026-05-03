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


package com.firefly.common.config.core.services;

import com.firefly.common.config.interfaces.dtos.ChannelConfigParameterDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for ChannelConfigParameter operations
 */
public interface ChannelConfigParameterService {

    /**
     * Get a channel config parameter by ID
     */
    Mono<ChannelConfigParameterDTO> getById(UUID id);

    /**
     * Filter channel config parameters with pagination
     */
    Mono<PaginationResponse<ChannelConfigParameterDTO>> filter(FilterRequest<ChannelConfigParameterDTO> filterRequest);

    /**
     * Create a new channel config parameter
     */
    Mono<ChannelConfigParameterDTO> create(ChannelConfigParameterDTO dto);

    /**
     * Update an existing channel config parameter
     */
    Mono<ChannelConfigParameterDTO> update(UUID id, ChannelConfigParameterDTO dto);

    /**
     * Delete a channel config parameter (soft delete)
     */
    Mono<Void> delete(UUID id);
}

