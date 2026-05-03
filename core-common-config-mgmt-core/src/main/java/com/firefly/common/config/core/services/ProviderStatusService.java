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

import com.firefly.common.config.interfaces.dtos.ProviderStatusDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing provider statuses
 */
public interface ProviderStatusService {

    /**
     * Get a provider status by ID
     * @param id Provider status ID
     * @return Provider status DTO
     */
    Mono<ProviderStatusDTO> getById(UUID id);

    /**
     * Filter provider statuses based on criteria
     * @param filterRequest Filter criteria
     * @return Paginated list of provider statuses
     */
    Mono<PaginationResponse<ProviderStatusDTO>> filter(FilterRequest<ProviderStatusDTO> filterRequest);

    /**
     * Create a new provider status
     * @param providerStatusDTO Provider status DTO
     * @return Created provider status DTO
     */
    Mono<ProviderStatusDTO> create(ProviderStatusDTO providerStatusDTO);

    /**
     * Update an existing provider status
     * @param id Provider status ID
     * @param providerStatusDTO Provider status DTO
     * @return Updated provider status DTO
     */
    Mono<ProviderStatusDTO> update(UUID id, ProviderStatusDTO providerStatusDTO);

    /**
     * Delete a provider status
     * @param id Provider status ID
     * @return Void
     */
    Mono<Void> delete(UUID id);
}
