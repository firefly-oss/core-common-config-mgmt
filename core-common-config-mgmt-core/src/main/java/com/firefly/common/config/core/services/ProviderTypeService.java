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

import com.firefly.common.config.interfaces.dtos.ProviderTypeDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing provider types
 */
public interface ProviderTypeService {

    /**
     * Get a provider type by ID
     * @param id Provider type ID
     * @return Provider type DTO
     */
    Mono<ProviderTypeDTO> getById(UUID id);

    /**
     * Filter provider types based on criteria
     * @param filterRequest Filter criteria
     * @return Paginated list of provider types
     */
    Mono<PaginationResponse<ProviderTypeDTO>> filter(FilterRequest<ProviderTypeDTO> filterRequest);

    /**
     * Create a new provider type
     * @param providerTypeDTO Provider type DTO
     * @return Created provider type DTO
     */
    Mono<ProviderTypeDTO> create(ProviderTypeDTO providerTypeDTO);

    /**
     * Update an existing provider type
     * @param id Provider type ID
     * @param providerTypeDTO Provider type DTO
     * @return Updated provider type DTO
     */
    Mono<ProviderTypeDTO> update(UUID id, ProviderTypeDTO providerTypeDTO);

    /**
     * Delete a provider type
     * @param id Provider type ID
     * @return Void
     */
    Mono<Void> delete(UUID id);
}
