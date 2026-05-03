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

import com.firefly.common.config.interfaces.dtos.ProviderDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing providers
 */
public interface ProviderService {

    /**
     * Get a provider by ID
     * @param id Provider ID
     * @return Provider DTO
     */
    Mono<ProviderDTO> getById(UUID id);

    /**
     * Filter providers based on criteria
     * @param filterRequest Filter criteria
     * @return Paginated list of providers
     */
    Mono<PaginationResponse<ProviderDTO>> filter(FilterRequest<ProviderDTO> filterRequest);

    /**
     * Create a new provider
     * @param providerDTO Provider DTO
     * @return Created provider DTO
     */
    Mono<ProviderDTO> create(ProviderDTO providerDTO);

    /**
     * Update an existing provider
     * @param id Provider ID
     * @param providerDTO Provider DTO
     * @return Updated provider DTO
     */
    Mono<ProviderDTO> update(UUID id, ProviderDTO providerDTO);

    /**
     * Delete a provider
     * @param id Provider ID
     * @return Void
     */
    Mono<Void> delete(UUID id);
}
