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

import com.firefly.common.config.interfaces.dtos.ProviderTenantDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing provider-tenant relationships
 */
public interface ProviderTenantService {

    /**
     * Get a provider-tenant relationship by ID
     * @param id Provider-Tenant relationship ID
     * @return Provider-Tenant DTO
     */
    Mono<ProviderTenantDTO> getById(UUID id);

    /**
     * Filter provider-tenant relationships based on criteria
     * @param filterRequest Filter criteria
     * @return Paginated list of provider-tenant relationships
     */
    Mono<PaginationResponse<ProviderTenantDTO>> filter(FilterRequest<ProviderTenantDTO> filterRequest);

    /**
     * Create a new provider-tenant relationship
     * @param providerTenantDTO Provider-Tenant DTO
     * @return Created provider-tenant DTO
     */
    Mono<ProviderTenantDTO> create(ProviderTenantDTO providerTenantDTO);

    /**
     * Update an existing provider-tenant relationship
     * @param id Provider-Tenant relationship ID
     * @param providerTenantDTO Provider-Tenant DTO
     * @return Updated provider-tenant DTO
     */
    Mono<ProviderTenantDTO> update(UUID id, ProviderTenantDTO providerTenantDTO);

    /**
     * Delete a provider-tenant relationship
     * @param id Provider-Tenant relationship ID
     * @return Void
     */
    Mono<Void> delete(UUID id);
}

