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

import com.firefly.common.config.interfaces.dtos.TenantDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Service interface for managing tenants
 */
public interface TenantService {

    /**
     * Get a tenant by ID
     * @param id Tenant ID
     * @return Tenant DTO
     */
    Mono<TenantDTO> getById(UUID id);

    /**
     * Filter tenants based on criteria
     * @param filterRequest Filter criteria
     * @return Paginated list of tenants
     */
    Mono<PaginationResponse<TenantDTO>> filter(FilterRequest<TenantDTO> filterRequest);

    /**
     * Create a new tenant
     * @param tenantDTO Tenant DTO
     * @return Created tenant DTO
     */
    Mono<TenantDTO> create(TenantDTO tenantDTO);

    /**
     * Update an existing tenant
     * @param id Tenant ID
     * @param tenantDTO Tenant DTO
     * @return Updated tenant DTO
     */
    Mono<TenantDTO> update(UUID id, TenantDTO tenantDTO);

    /**
     * Delete a tenant
     * @param id Tenant ID
     * @return Void
     */
    Mono<Void> delete(UUID id);
}

