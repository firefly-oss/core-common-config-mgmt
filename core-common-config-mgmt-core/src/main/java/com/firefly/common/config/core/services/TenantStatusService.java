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

import com.firefly.common.config.interfaces.dtos.TenantStatusDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing tenant statuses
 */
public interface TenantStatusService {

    /**
     * Get a tenant status by ID
     * @param id Tenant status ID
     * @return Tenant status DTO
     */
    Mono<TenantStatusDTO> getById(UUID id);

    /**
     * Filter tenant statuses based on criteria
     * @param filterRequest Filter criteria
     * @return Paginated list of tenant statuses
     */
    Mono<PaginationResponse<TenantStatusDTO>> filter(FilterRequest<TenantStatusDTO> filterRequest);

    /**
     * Create a new tenant status
     * @param tenantStatusDTO Tenant status DTO
     * @return Created tenant status DTO
     */
    Mono<TenantStatusDTO> create(TenantStatusDTO tenantStatusDTO);

    /**
     * Update an existing tenant status
     * @param id Tenant status ID
     * @param tenantStatusDTO Tenant status DTO
     * @return Updated tenant status DTO
     */
    Mono<TenantStatusDTO> update(UUID id, TenantStatusDTO tenantStatusDTO);

    /**
     * Delete a tenant status
     * @param id Tenant status ID
     * @return Void
     */
    Mono<Void> delete(UUID id);
}

