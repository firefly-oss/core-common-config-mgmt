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

import com.firefly.common.config.interfaces.dtos.ApiProcessMappingDTO;
import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service interface for managing API-to-Process mappings.
 * 
 * <p>This service provides CRUD operations for process mappings and
 * resolution logic for finding the best matching mapping for a request.</p>
 * 
 * @author Firefly Development Team
 * @since 1.0.0
 */
public interface ApiProcessMappingService {

    /**
     * Gets a mapping by ID.
     * 
     * @param id the mapping ID
     * @return Mono of the mapping DTO
     */
    Mono<ApiProcessMappingDTO> getById(UUID id);

    /**
     * Filters mappings based on the filter request.
     * 
     * @param filterRequest the filter criteria
     * @return paginated response of mappings
     */
    Mono<PaginationResponse<ApiProcessMappingDTO>> filter(FilterRequest<ApiProcessMappingDTO> filterRequest);

    /**
     * Creates a new mapping.
     * 
     * @param dto the mapping to create
     * @return Mono of the created mapping
     */
    Mono<ApiProcessMappingDTO> create(ApiProcessMappingDTO dto);

    /**
     * Updates an existing mapping.
     * 
     * @param id the mapping ID
     * @param dto the updated mapping
     * @return Mono of the updated mapping
     */
    Mono<ApiProcessMappingDTO> update(UUID id, ApiProcessMappingDTO dto);

    /**
     * Deletes a mapping.
     * 
     * @param id the mapping ID
     * @return Mono that completes when deletion is done
     */
    Mono<Void> delete(UUID id);

    /**
     * Resolves the best matching mapping for an operation.
     * 
     * @param tenantId the tenant ID
     * @param operationId the operation ID
     * @param productId the product ID (optional)
     * @param channelType the channel type (optional)
     * @return Mono of the best matching mapping
     */
    Mono<ApiProcessMappingDTO> resolveMapping(
            UUID tenantId, 
            String operationId, 
            UUID productId, 
            String channelType);

    /**
     * Gets all mappings for a tenant.
     * 
     * @param tenantId the tenant ID
     * @return Flux of tenant mappings
     */
    Flux<ApiProcessMappingDTO> getByTenantId(UUID tenantId);

    /**
     * Gets all vanilla (default) mappings.
     * 
     * @return Flux of vanilla mappings
     */
    Flux<ApiProcessMappingDTO> getVanillaMappings();

    /**
     * Gets all mappings for a process.
     * 
     * @param processId the process ID
     * @return Flux of mappings
     */
    Flux<ApiProcessMappingDTO> getByProcessId(String processId);

    /**
     * Invalidates cache for a tenant.
     * 
     * @param tenantId the tenant ID (null for all)
     * @return Mono that completes when invalidation is done
     */
    Mono<Void> invalidateCache(UUID tenantId);
}
